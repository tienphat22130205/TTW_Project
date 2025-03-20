package vn.edu.hcmuaf.fit.project_fruit.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordController", value = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        // Xử lý khôi phục mật khẩu
        String newPassword = userService.recoverPassword(email);

        if (newPassword != null) {
            // Hiển thị mật khẩu mới trên giao diện
            request.setAttribute("successMessage", "Mật khẩu mới của bạn là: " + newPassword);
        } else {
            request.setAttribute("errorMessage", "Không thể khôi phục mật khẩu. Email không tồn tại hoặc có lỗi.");
        }

        // Chuyển về trang khôi phục mật khẩu
        request.getRequestDispatcher("/user/forgotPassword.jsp").forward(request, response);
    }
}
