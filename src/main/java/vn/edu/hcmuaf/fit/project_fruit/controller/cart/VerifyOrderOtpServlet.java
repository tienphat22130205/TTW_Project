package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verify-order-otp")
public class VerifyOrderOtpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String enteredOtp = req.getParameter("otp");
        String sessionOtp = (String) req.getSession().getAttribute("order_otp");

        resp.setContentType("text/plain;charset=UTF-8");

        if (enteredOtp != null && enteredOtp.equals(sessionOtp)) {
            req.getSession().setAttribute("otp_verified", true);
            resp.getWriter().write("success");
        } else {
            resp.getWriter().write("Mã OTP không đúng hoặc đã hết hạn.");
        }
    }
}

