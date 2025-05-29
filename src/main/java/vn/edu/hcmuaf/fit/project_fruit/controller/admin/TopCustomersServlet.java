package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/top-customers")
public class TopCustomersServlet extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Map<String, Object>> topCustomers = invoiceService.getTopSpendingCustomers(5);
        System.out.println("TOP CUSTOMERS SIZE: " + topCustomers.size()); // THÊM DÒNG NÀY

        req.setAttribute("topCustomers", topCustomers);
        req.getRequestDispatcher("/admin/admin.jsp").forward(req, resp);
    }
}

