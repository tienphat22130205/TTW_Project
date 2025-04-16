package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;

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
        String code = request.getParameter("promotion_code");
        String description = request.getParameter("description_add");
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("expiration_date");
        String discountPercent = request.getParameter("promotion_discount");
        String type = request.getParameter("promotion_type");
        String minOrderAmount = request.getParameter("min_order_amount");
        String maxUsage = request.getParameter("max_usage");

        // Câu lệnh SQL thêm khuyến mãi
        String query = "INSERT INTO promotions (promotion_name, code, describe_1, start_date, end_date, percent_discount, type, min_order_amount, max_usage, usage_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, promotionName);
            ps.setString(2, code);
            ps.setString(3, description);
            ps.setString(4, startDate);
            ps.setString(5, endDate);
            ps.setDouble(6, Double.parseDouble(discountPercent));
            ps.setString(7, type);
            ps.setDouble(8, Double.parseDouble(minOrderAmount));
            ps.setInt(9, Integer.parseInt(maxUsage));

            // Thực thi lệnh INSERT
            ps.executeUpdate();

            // Điều hướng về trang admin
            response.sendRedirect(request.getContextPath() + "/admin#");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin#?status=error");
        }
    }
}
