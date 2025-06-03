package vn.edu.hcmuaf.fit.project_fruit.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("errorMessage", " Mật khẩu xác nhận không khớp!");
        } else {
            // ✅ Kiểm tra xem mật khẩu mới có bị trùng mật khẩu gần đây không
            if (userService.isPasswordRecentlyUsed(user.getEmail(), newPassword)) {
                req.setAttribute("errorMessage", " Mật khẩu này đã được sử dụng trong vòng 1 tháng. Vui lòng chọn mật khẩu khác.");
            } else {
                boolean result = userService.changePassword(user.getEmail(), currentPassword, newPassword);
                if (result) {
                    req.setAttribute("successMessage", " Đổi mật khẩu thành công!");

                    // Ghi log
                    try (Connection conn = DbConnect.getConnection()) {
                        LogsDao logsDao = new LogsDao(conn);
                        Logs log = new Logs();
                        log.setUserId(user.getId_account());
                        log.setLevel("INFO");
                        log.setAction("change_password");
                        log.setResource("user_" + user.getId_account());
                        log.setBeforeData("");
                        log.setAfterData("Người dùng ID#" + user.getId_account() +" đã thay đổi mật khẩu");
                        log.setRole(user.getRole());
                        log.setSeen(false);
                        logsDao.insertLog(log);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                } else {
                    req.setAttribute("errorMessage", " Mật khẩu hiện tại không đúng!");
                }
            }
        }

        // Đặt lại active section là "change-password"
        req.setAttribute("activeSection", "change-password");
        req.getRequestDispatcher("/user/user.jsp").forward(req, resp);
    }
}
