package vn.edu.hcmuaf.fit.project_fruit.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.ProductDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
import vn.edu.hcmuaf.fit.project_fruit.service.ProductService;
import vn.edu.hcmuaf.fit.project_fruit.dao.FeedbackDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Feedback;
import java.util.List;

import java.io.IOException;

@WebServlet(name = "ProductDetail", value = "/product-detail")
public class ProductDetail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");

        if (pid == null || pid.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required.");
            return;
        }
        ProductService service = new ProductService();
        ProductDao dao = new ProductDao();
        try {
            int id = Integer.parseInt(pid);
            System.out.println(id);
            Product product = service.getDetails(id);

            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            request.setAttribute("product", product);
            System.out.println(product);


            // Lấy danh mục (categoryId) của sản phẩm
            int categoryId = dao.getCategoryIdByProductId(id);
            if (categoryId == -1) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found for this product");
                return;
            }
            // Lấy danh sách sản phẩm liên quan
            List<Product> relatedProducts = service.getRelatedProducts(categoryId, id);
            if (relatedProducts.isEmpty()) {
                request.setAttribute("relatedMessage", "No related products found.");
            }
            request.setAttribute("relatedProducts", relatedProducts);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format.");
        }

         FeedbackDao feedbackDao = new FeedbackDao();
        // Lấy danh sách phản hồi theo id sản phẩm
        List<Feedback> feedbacks = feedbackDao.getFeedbackByProductId(Integer.parseInt(pid));
        // Đưa danh sách phản hồi vào request
        request.setAttribute("idProduct", pid);
        request.setAttribute("feedbacks", feedbacks);

        request.getRequestDispatcher("/product/detailProduct.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không sử dụng doPost trong trường hợp này
    }
}
