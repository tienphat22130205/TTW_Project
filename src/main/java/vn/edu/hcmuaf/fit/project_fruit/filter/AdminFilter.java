package vn.edu.hcmuaf.fit.project_fruit.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Chưa đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login.jsp"); // Về trang login
            return;
        }

        // Đã đăng nhập nhưng không phải admin
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/user/login.jsp?error=unauthorized");
            return;
        }

        // Đúng quyền -> cho đi tiếp
        chain.doFilter(request, response);
    }
}
