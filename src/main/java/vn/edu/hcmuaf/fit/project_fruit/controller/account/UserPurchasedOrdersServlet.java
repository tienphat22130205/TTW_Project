package vn.edu.hcmuaf.fit.project_fruit.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.Map;
import java.util.List;


@WebServlet("/user/purchased-orders")
public class UserPurchasedOrdersServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<Map<String, Object>> orders = invoiceService.getPurchasedProductsByUserId(user.getId_account());
        req.setAttribute("orders", orders);

        boolean isAjax = "true".equals(req.getParameter("ajax"));
        if (isAjax) {
            // Trả về fragment html riêng cho ajax load
            req.getRequestDispatcher("/WEB-INF/views/user/orders_fragment.jsp").forward(req, resp);
        } else {
            // Trả về trang đầy đủ
            req.setAttribute("activeSection", "order-management");
            req.getRequestDispatcher("/user/user.jsp").forward(req, resp);
        }
    }
}
