package vn.edu.hcmuaf.fit.project_fruit.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.project_fruit.dao.UserDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.util.UUID;

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
        // 1. Kiểm tra email đã tồn tại
        if (userDao.isEmailExists(email)) {
            System.out.println("❌ Email đã tồn tại: " + email);
            return false;
        }

        // 2. Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Mật khẩu xác nhận không khớp");
            return false;
        }

        // 3. Kiểm tra độ mạnh của mật khẩu
        if (!isStrongPassword(password)) {
            System.out.println("❌ Mật khẩu phải dài ít nhất 8 ký tự, chứa cả chữ và số");
            return false;
        }

        // 4. Hash mật khẩu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 5. Tạo đối tượng User
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setRole("user"); // Mặc định là user

        // 6. Gọi DAO để lưu vào database
        boolean result = userDao.registerUser(newUser, fullName);

        if (result) {
            System.out.println("✅ Đăng ký thành công: " + email);
        } else {
            System.out.println("❌ Đăng ký thất bại trong DAO");
        }

        return result;
    }
    // ✅ Hàm kiểm tra mật khẩu mạnh
    public boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Za-z].*") &&
                password.matches(".*[0-9].*");
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
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
    public boolean linkGoogleAccount(String email, String googleId) {
        return userDao.linkGoogleAccount(email, googleId);
    }

}


    