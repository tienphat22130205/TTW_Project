package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/AddPromotionServlet")
public class AddPromotion extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String promotionName = request.getParameter("promotion_name");
        String description = request.getParameter("description_add");
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("expiration_date");
        String discountPercent = request.getParameter("promotion_discount");
        String type = request.getParameter("promotion_type");
        String promotion_code= request.getParameter("promotion_code_input");

        // Câu lệnh SQL để thêm dữ liệu vào cơ sở dữ liệu
        String query = "INSERT INTO promotions (promotion_name, describe_1, start_date, end_date, percent_discount, type,promotion_code) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ps.setString(1, promotionName);
            ps.setString(2, description);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.setString(5, discountPercent);
            ps.setString(6, type);
            ps.setString(7, promotion_code);


            // Thực hiện thêm dữ liệu
            ps.executeUpdate();


            HttpSession session = request.getSession(false);
            int userId = 0;
            String role = "";
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    userId = user.getId_account();
                    role = user.getRole(); // giả sử có getRole()
                }
            }
            // Tạo log mới
            LogsDao logsDao = null;
            try (var conn = DbConnect.getConnection()) {
                logsDao = new LogsDao(conn);
                Logs log = new Logs();
                log.setUserId(userId);
                log.setLevel("INFO");
                log.setAction("Thêm khuyến mãi mới");
                log.setResource(promotionName);
                log.setBeforeData("");
                log.setAfterData("Mã: " + promotionName + ", Mô tả: " + description + ", Giảm: " + discountPercent + "%");
                log.setRole(role);
                logsDao.insertLog(log);
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Điều hướng quay lại trang admin với anchor #
            response.sendRedirect(request.getContextPath() + "/admin#");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin#");
        }
    }
}
