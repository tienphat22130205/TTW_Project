package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/project_fruit/vnpay-return")
public class VnpayReturnServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String code = req.getParameter("vnp_ResponseCode");
        if ("00".equals(code)) {
            req.setAttribute("message", "✅ Thanh toán thành công!");
        } else {
            req.setAttribute("message", "❌ Thanh toán thất bại.");
        }
        req.getRequestDispatcher("/user/payment.jsp").forward(req, resp);
    }
}

