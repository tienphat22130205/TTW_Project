package vn.edu.hcmuaf.fit.project_fruit.controller.login;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;

import java.io.IOException;

@WebServlet("/google-login")
public class GoogleController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            try {
                // Lấy access token từ Google
                String accessToken = GoogleLogin.getToken(code);

                // Lấy thông tin user từ Google
                JsonObject googleUserInfo = GoogleLogin.getUserInfo(accessToken);
                String email = googleUserInfo.get("email").getAsString();
                String googleId = googleUserInfo.get("id").getAsString();
                String name = googleUserInfo.has("name") ? googleUserInfo.get("name").getAsString() : "Unknown";

                // Kiểm tra user trong database
                User user = userService.getUserByEmail(email);
                if (user == null) {
                    // Nếu chưa có, tự động đăng ký tài khoản
                    String randomPassword = userService.generateRandomPassword();
                    String hashedPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPassword(hashedPassword);
                    newUser.setRole("user");
                    newUser.setGoogleId(googleId);

                    boolean registered = userService.registerUser(email, randomPassword, randomPassword, name);
                    if (!registered) {
                        response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=Đăng ký thất bại.");
                        return;
                    }

                    user = userService.getUserByEmail(email);
                } else {
                    // Nếu user đã có nhưng chưa liên kết Google ID thì cập nhật
                    if (user.getGoogleId() == null) {
                        userService.linkGoogleAccount(email, googleId);
                    }
                }

                // Lưu thông tin user vào session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Chuyển hướng về trang chủ
                response.sendRedirect(request.getContextPath() + "/home");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=Đăng nhập bằng Google thất bại.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        }
    }
}
