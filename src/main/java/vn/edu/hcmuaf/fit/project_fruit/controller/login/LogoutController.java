package vn.edu.hcmuaf.fit.project_fruit.controller.login;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LogoutController", value = "/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Lấy session hiện tại
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Lấy user từ session trước khi invalidate
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // Tạo log logout
                Logs log = new Logs(
                        user.getId_account(),
                        "INFO",
                        "Login",
                        "accounts",
                        null,
                        null,
                        user.getRole(),
                        false
                );
                LogsDao logDao = new LogsDao(DbConnect.getConnection());
                try {
                    logDao.insertLog(log);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Có thể ghi log lỗi hoặc xử lý tiếp nếu cần
                }
            }
            String logoutMessage = "Đăng xuất thành công!";

            // Invalidate session cũ
            session.invalidate();

            // Tạo session mới để lưu thông báo
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("logoutMessage", logoutMessage);
        }


        // Chuyển hướng người dùng về trang danh sách sản phẩm (list-product)
        response.sendRedirect("home");
    }
}
