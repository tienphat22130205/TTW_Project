package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
import vn.edu.hcmuaf.fit.project_fruit.service.ProductService;

import java.io.IOException;

@WebServlet(name = "AddAjax", value = "/add-cart-ajax")
public class AddAjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int productId = Integer.parseInt(request.getParameter("addToCartPid"));
            int quantity = 1;
            String quantityParam = request.getParameter("quantity");
            if (quantityParam != null) {
                try {
                    quantity = Integer.parseInt(quantityParam);
                } catch (NumberFormatException e) {
                    quantity = 1;
                }
            }

            ProductService productService = new ProductService();
            Product product = productService.getDetails(productId);

            if (product == null) {
                response.getWriter().write("{\"success\":false,\"message\":\"Sản phẩm không tồn tại.\"}");
                return;
            }

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                cart = new Cart();
            }

            cart.addProduct(product, quantity);
            session.setAttribute("cart", cart);

            int totalQuantity = cart.getTotalQuantity();

            String json = String.format("{\"success\":true,\"message\":\"Đã thêm sản phẩm vào giỏ hàng.\",\"totalQuantity\":%d}", totalQuantity);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\":false,\"message\":\"Tham số không hợp lệ.\"}");
        }
    }
}
