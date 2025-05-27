package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/revenue-by-payment-method")
public class PaymentMethodRevenueServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Double> revenueData = invoiceService.getRevenueByPaymentMethod();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        new Gson().toJson(revenueData, resp.getWriter());
    }
}
