package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/dashboard-summary")
public class DashboardSummaryServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> summary = invoiceService.getDashboardSummaryThisMonth();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        new Gson().toJson(summary, resp.getWriter());
    }
}
