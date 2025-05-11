package vn.edu.hcmuaf.fit.project_fruit.controller.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        if (userService.verifyOtp(email, otp)) {
            request.setAttribute("message", "✅ Xác thực OTP thành công!");
        } else {
            request.setAttribute("message", "❌ Mã OTP không chính xác hoặc đã hết hạn.");
        }

        request.setAttribute("message", "Xác thực thành công. Bạn có thể đăng nhập.");
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }
}

