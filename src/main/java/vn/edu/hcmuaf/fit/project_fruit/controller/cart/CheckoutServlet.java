package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.ShippingMethodDAO;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.ShippingMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

        // Bước 3: Truy vấn danh sách phương thức vận chuyển từ database
        ShippingMethodDAO shippingMethodDAO = new ShippingMethodDAO();
        List<ShippingMethod> shippingMethods = shippingMethodDAO.getAllShippingMethods();

        // Đưa danh sách phương thức vận chuyển vào request
        request.setAttribute("shippingMethods", shippingMethods);

        // Bước 4: Chuyển đến trang thanh toán
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);

    }
}

