package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.utils.EmailOtpUtils;

import java.io.IOException;
import java.util.Random;

@WebServlet("/send-order-otp")
public class SendOrderOtpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        resp.setContentType("text/plain;charset=UTF-8");

        // Kiểm tra định dạng email
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            resp.getWriter().write("Email không hợp lệ");
            return;
        }

        // Tạo mã OTP ngẫu nhiên
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 chữ số

        // Lưu vào session
        req.getSession().setAttribute("order_otp", otp);
        req.getSession().setAttribute("order_email", email);

        // Gửi email
        boolean success = EmailOtpUtils.sendOrderOtp(email, otp);
        if (success) {
            resp.getWriter().write("Đã gửi mã xác minh về email. Vui lòng kiểm tra hộp thư.");
        } else {
            resp.getWriter().write("Lỗi khi gửi email. Vui lòng thử lại sau.");
        }
    }
}
