package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/order-status-month")
public class OrderStatusChartServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> statusMap = invoiceService.getOrderStatusCountThisMonth();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        new Gson().toJson(statusMap, resp.getWriter());
    }
}
