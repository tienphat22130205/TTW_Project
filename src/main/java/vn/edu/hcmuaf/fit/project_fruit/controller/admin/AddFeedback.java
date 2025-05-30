package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.FeedbackDao; // ✅ import thêm
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/AddFeedbackServlet")
public class AddFeedback extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String content = request.getParameter("content");
        String ratingStr = request.getParameter("rating");
        String idProductStr = request.getParameter("productId");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=not_logged_in");
            return;
        }

        int idAccount = user.getId_account();

        try {
            if (content == null || content.trim().isEmpty() ||
                    ratingStr == null || ratingStr.trim().isEmpty() ||
                    idProductStr == null || idProductStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=invalid_input");
                return;
            }

            int idProduct = Integer.parseInt(idProductStr);
            double rating = Double.parseDouble(ratingStr);

            // ✅ Kiểm tra xem tài khoản đã mua sản phẩm này chưa
            FeedbackDao feedbackDao = new FeedbackDao();
            boolean hasPurchased = feedbackDao.hasUserPurchasedProduct(idAccount, idProduct);
            if (!hasPurchased) {
                response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=not_purchased");
                return;
            }

            LocalDate today = LocalDate.now();

            String query = "INSERT INTO feedbacks (id_product, id_account, content, date_create, rating) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = DbConnect.getPreparedStatement(query, true)) {
                preparedStatement.setInt(1, idProduct);
                preparedStatement.setInt(2, idAccount);
                preparedStatement.setString(3, content);
                preparedStatement.setDate(4, java.sql.Date.valueOf(today));
                preparedStatement.setDouble(5, rating);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=insert_failed");
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=invalid_format");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?pid=" + idProductStr + "&message=error");
        }
    }
}
