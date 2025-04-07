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
import java.io.PrintWriter;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Bước 1: Kiểm tra người dùng đăng nhập
        Object user = session.getAttribute("user");
        if (user == null) {
            // Trả về mã lỗi 401 để client tự xử lý
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"unauthorized\", \"message\": \"Bạn cần đăng nhập để thanh toán.\"}");
            out.flush();
            return;
        }
        // Bước 2: Kiểm tra giỏ hàng
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getList().isEmpty()) {
            response.sendRedirect("show-cart");
            return;
        }

        // Bước 3: Chuyển đến trang thanh toán
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);


        for (CartProduct cp : cart.getList()) {
            System.out.println("Tên: " + cp.getName());
            System.out.println("Giá: " + cp.getPrice());
            System.out.println("Số lượng: " + cp.getQuantity());
            if (cp.getListImg() != null && !cp.getListImg().isEmpty()) {
                System.out.println("Ảnh: " + cp.getListImg().get(0).getUrl());
            } else {
                System.out.println("Không có ảnh");
            }
        }
    }
}

