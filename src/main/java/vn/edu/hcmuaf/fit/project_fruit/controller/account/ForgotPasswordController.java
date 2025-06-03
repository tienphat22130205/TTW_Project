package vn.edu.hcmuaf.fit.project_fruit.controller.account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;
import vn.edu.hcmuaf.fit.project_fruit.utils.EmailUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String newPassword = userService.recoverPassword(email);

        HttpSession session = request.getSession();

        if (newPassword != null) {
            String html = "<p>Bạn đã yêu cầu khôi phục mật khẩu tại VitaminFruit.</p>"
                    + "<p>Mật khẩu mới của bạn là: <b>" + newPassword + "</b></p>"
                    + "<p>Hãy đăng nhập và đổi lại mật khẩu.</p>";

            EmailUtils.sendEmail(email, "Khôi phục mật khẩu - VitaminFruit", html);

            session.setAttribute("successMessage", "Mật khẩu mới đã được gửi tới email của bạn.");
            try (Connection conn = DbConnect.getConnection()) {
                User user = new User();
                LogsDao logsDao = new LogsDao(conn);
                Logs log = new Logs();
                log.setUserId(user.getId_account());
                log.setLevel("INFO");
                log.setAction("forgot_password_success");
                log.setResource( email);
                log.setBeforeData("");
                log.setAfterData("Người dùng " + email +" đã được cấp lại mật khẩu");
                log.setRole("User");
                log.setSeen(false);
                logsDao.insertLog(log);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            session.setAttribute("errorMessage", "Email không tồn tại hoặc lỗi khi gửi.");
        }

        // Dùng redirect để tránh lặp lại khi reload
        response.sendRedirect(request.getContextPath() + "/user/forgotPassword.jsp");
    }
}
