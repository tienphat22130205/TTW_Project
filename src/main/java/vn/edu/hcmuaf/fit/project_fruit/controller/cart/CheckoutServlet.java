package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;

import java.io.IOException;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");

        // Nếu giỏ hàng trống, chuyển hướng về trang giỏ hàng
        if (cart == null || cart.getList().isEmpty()) {
            response.sendRedirect("show-cart");
            return;
        }

        // Log kiểm tra dữ liệu giỏ hàng
        for (CartProduct product : cart.getList()) {
            System.out.println("Product Name: " + product.getName());
            System.out.println("Product Price: " + product.getPrice());
            System.out.println("Product Quantity: " + product.getQuantity());
            if (!product.getListImg().isEmpty()) {
                System.out.println("Product Image URL: " + product.getListImg().get(0).getUrl());
            } else {
                System.out.println("No image available for this product.");
            }
        }

        // Truyền đối tượng `cart` sang JSP
        request.setAttribute("cart", cart);

        // Chuyển tới `payment.jsp`
        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
    }
}





