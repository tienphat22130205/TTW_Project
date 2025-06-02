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
        String promotionName = request.getParameter("promotion_name");
        String promotionCode = request.getParameter("promotion_code-input");
        String description = request.getParameter("description_add");
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("expiration_date");
        String discountPercent = request.getParameter("promotion_discount");
        String minOrderAmount = request.getParameter("min_order_amount");
        String maxUsage = request.getParameter("max_usage");

        String query = "INSERT INTO promotions (promotion_name, promotion_code, describe_1, start_date, end_date, percent_discount, min_order_amount, max_usage) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ps.setString(1, promotionName);
            ps.setString(2, promotionCode);
            ps.setString(3, description);
            ps.setString(4, startDate);
            ps.setString(5, endDate);
            ps.setString(6, discountPercent);
            ps.setString(7, minOrderAmount);
            ps.setString(8, maxUsage);

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
            response.sendRedirect(request.getContextPath() + "/admin?msg=success");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin?msg=error");
        }
    }
}
