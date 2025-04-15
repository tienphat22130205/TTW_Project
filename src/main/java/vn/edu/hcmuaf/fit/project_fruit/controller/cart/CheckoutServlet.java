package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.ShippingMethodDAO;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.ShippingMethod;
import vn.edu.hcmuaf.fit.project_fruit.service.PromotionService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PromotionService promotionService = new PromotionService();
        List<Promotions> promotionList = promotionService.getAll();
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("promotionList", promotionList);

        HttpSession session = request.getSession();

        // Bước 1: Kiểm tra người dùng đăng nhập
        Object user = session.getAttribute("user");
        if (user == null) {
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

        // Bước 3: Truy vấn danh sách phương thức vận chuyển
        ShippingMethodDAO shippingMethodDAO = new ShippingMethodDAO();
        List<ShippingMethod> shippingMethods = shippingMethodDAO.getAllShippingMethods();
        request.setAttribute("shippingMethods", shippingMethods);

        // Bước 4: Tính toán tổng tiền
        double tempTotal = cart.getTotalPrice(); // Tạm tính

        // Xử lý discount an toàn (int hoặc double)
        double discount = 0;
        Object discountObj = session.getAttribute("discount");
        if (discountObj != null) {
            if (discountObj instanceof Integer) {
                discount = (int) discountObj;
            } else if (discountObj instanceof Double) {
                discount = (double) discountObj;
            }
        }

        // Phí vận chuyển
        double shippingFee = 0;
        String selectedShippingId = request.getParameter("shipping_method");
        if (selectedShippingId != null && !selectedShippingId.isEmpty()) {
            session.setAttribute("shipping_method", selectedShippingId);
        } else {
            selectedShippingId = (String) session.getAttribute("shipping_method");
        }


        if (selectedShippingId != null && !selectedShippingId.isEmpty()) {
            try {
                int id = Integer.parseInt(selectedShippingId);
                for (ShippingMethod sm : shippingMethods) {
                    if (sm.getId() == id) {
                        shippingFee = sm.getShippingFee();
                        break;
                    }
                }
            } catch (NumberFormatException ignored) {}
        }


        double finalTotal = tempTotal - discount + shippingFee;

        // Bước 5: Truyền dữ liệu sang JSP
        request.setAttribute("cart", cart);
        request.setAttribute("tempTotal", tempTotal);
        request.setAttribute("discount", discount);
        request.setAttribute("shippingFee", shippingFee);
        request.setAttribute("finalTotal", finalTotal);

        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
    }
}
