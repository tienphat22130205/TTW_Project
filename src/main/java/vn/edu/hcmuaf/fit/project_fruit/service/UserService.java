package vn.edu.hcmuaf.fit.project_fruit.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.project_fruit.dao.UserDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.auth.FacebookAuthHelper;
import vn.edu.hcmuaf.fit.project_fruit.dao.auth.GoogleAuthHelper;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import java.util.UUID;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.security.GeneralSecurityException;
import java.io.IOException;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User validateUser(String email, String password) {
        // Lấy user từ database
        User user = userDao.getUserByEmailAndPassword(email, null); // Không kiểm tra mật khẩu trong query

        if (user == null) {
            System.out.println("Không tìm thấy user với email: " + email);
            return null;
        }

        // Kiểm tra mật khẩu bằng BCrypt
        if (!BCrypt.checkpw(password, user.getPassword())) {
            System.out.println("Mật khẩu không chính xác.");
            return null;
        }

        // Kiểm tra vai trò hợp lệ
        if (!"admin".equals(user.getRole()) && !"user".equals(user.getRole()) && !"staff".equals(user.getRole())) {
            System.out.println("Vai trò không hợp lệ: " + user.getRole());
            return null;
        }

        return user; // Trả về user nếu hợp lệ
    }


    public boolean registerUser(String email, String password, String confirmPassword, String fullName) {
        if (userDao.isEmailExists(email)) {
            System.out.println("Email đã tồn tại: " + email);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Mật khẩu xác nhận không khớp");
            return false;
        }

        // Hash mật khẩu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Tạo đối tượng User mới
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setRole("user"); // Vai trò mặc định là "user"

        // Gọi DAO để thêm người dùng mới
        return userDao.registerUser(newUser, fullName);
    }
    // Tạo mật khẩu ngẫu nhiên
    public String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // Tạo chuỗi ngẫu nhiên 8 ký tự
    }

    // Phục hồi mật khẩu
    public String recoverPassword(String email) {
        // Kiểm tra email có tồn tại hay không
        if (!userDao.isEmailExists(email)) {
            return null; // Email không tồn tại
        }

        // Tạo mật khẩu mới
        String newPassword = generateRandomPassword();

        // Mã hóa mật khẩu mới
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Cập nhật mật khẩu vào cơ sở dữ liệu
        boolean isUpdated = userDao.updatePasswordByEmail(email, hashedPassword);

        if (isUpdated) {
            return newPassword; // Trả về mật khẩu gốc để gửi qua email
        }
        return null; // Cập nhật thất bại
    }
    public User validateGoogleUser(String idTokenString) {
        try {
            GoogleIdToken.Payload payload = GoogleAuthHelper.verifyToken(idTokenString);
            if (payload != null) {
                String email = payload.getEmail();
                String fullName = (String) payload.get("name");
                return findOrCreateUser(email, fullName, "google");
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User validateFacebookUser(String accessToken) {
        com.restfb.types.User facebookUser = FacebookAuthHelper.getUser(accessToken);
        if (facebookUser != null) {
            String email = facebookUser.getEmail();
            String fullName = facebookUser.getName();
            return findOrCreateUser(email, fullName, "facebook");
        }
        return null;
    }

    private User findOrCreateUser(String email, String fullName, String authProvider) {
        User user = userDao.getUserByEmail(email); // Thêm hàm getUserByEmail vào UserDao
        if (user == null) {
            // Tạo người dùng mới
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setRole("user");
            userDao.registerUser(newUser, fullName);
            user = userDao.getUserByEmail(email);
        }
        return user;
    }
}


    