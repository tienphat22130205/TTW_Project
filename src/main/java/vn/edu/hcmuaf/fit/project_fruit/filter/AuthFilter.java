package vn.edu.hcmuaf.fit.project_fruit.filter;//package vn.edu.hcmuaf.fit.project_fruit.filter;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
//
//import java.io.IOException;
//
//@WebFilter({"/admin/*", "/user/*"}) // Chỉ áp dụng cho các URL cần bảo vệ
//public class AuthFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        // Lấy session hiện tại
//        HttpSession session = httpRequest.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//
//        // Lấy URI của yêu cầu
//        String requestURI = httpRequest.getRequestURI();
//        String contextPath = httpRequest.getContextPath();
//
//        // Kiểm tra trạng thái đăng nhập
//        if (user == null) {
//            // Người dùng chưa đăng nhập, chuyển hướng đến trang login
//            httpResponse.sendRedirect(contextPath + "/login");
//            return;
//        }
//
//        // Kiểm tra vai trò (role) của người dùng
//        if ("admin".equals(user.getRole()) && requestURI.startsWith(contextPath + "/admin")) {
//            // Người dùng là admin, cho phép truy cập các URL admin
//            chain.doFilter(request, response);
//        } else if ("user".equals(user.getRole()) && requestURI.startsWith(contextPath + "/user")) {
//            // Người dùng là user, cho phép truy cập các URL user
//            chain.doFilter(request, response);
//        } else {
//            // Nếu không phù hợp, chuyển hướng đến trang không có quyền truy cập
//            httpResponse.sendRedirect(contextPath + "/unauthorized");
//        }
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {}
//
//    @Override
//    public void destroy() {}
//}
//
