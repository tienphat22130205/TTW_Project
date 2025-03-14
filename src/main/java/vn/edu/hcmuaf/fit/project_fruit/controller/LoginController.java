package vn.edu.hcmuaf.fit.project_fruit.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Customer;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import vn.edu.hcmuaf.fit.project_fruit.service.CustomerService;
import vn.edu.hcmuaf.fit.project_fruit.service.UserService;

import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {

    private final UserService userService = new UserService();
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị trang login
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("useremail");
        String password = request.getParameter("pass");
        String googleToken = request.getParameter("googleToken");
        String facebookToken = request.getParameter("facebookToken");

        User user = null;

        // Xử lý đăng nhập Google
        if (googleToken != null && !googleToken.isEmpty()) {
            user = userService.validateGoogleUser(googleToken);
        }
        // Xử lý đăng nhập Facebook
        else if (facebookToken != null && !facebookToken.isEmpty()) {
            user = userService.validateFacebookUser(facebookToken);
        }
        // Xử lý đăng nhập email/password truyền thống
        else if (email != null && password != null) {
            user = userService.validateUser(email, password);
        }
//        // Xác thực người dùng
//        User user = userService.validateUser(email, password);

        if (user != null) {
            // Lấy thông tin khách hàng từ bảng customers
            Customer customer = customerService.getCustomerById(user.getIdCustomer());

            if (customer != null) {
                // Lưu thông tin User và Customer vào session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("customer", customer);

                // Chuyển hướng theo vai trò
                if ("admin".equals(user.getRole()) || "staff".equals(user.getRole())) {
                    // Nếu vai trò là admin hoặc staff, chuyển đến trang admin
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else if ("user".equals(user.getRole())) {
                    // Nếu vai trò là user, chuyển đến trang home
                    response.sendRedirect(request.getContextPath() + "/home");
                } else {
                    // Nếu vai trò không hợp lệ, chuyển đến unauthorized
                    response.sendRedirect(request.getContextPath() + "/unauthorized");
                }
            } else {
                // Không tìm thấy thông tin khách hàng
                request.setAttribute("errorMessage", "Không thể tìm thấy thông tin khách hàng. Vui lòng thử lại.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
}
