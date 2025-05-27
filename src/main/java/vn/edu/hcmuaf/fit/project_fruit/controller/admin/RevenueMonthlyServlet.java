package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/revenue-monthly")
public class RevenueMonthlyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InvoiceService service = new InvoiceService();
        Map<Integer, Double> monthlyRevenue = service.getMonthlyRevenue();

        Gson gson = new Gson();
        String json = gson.toJson(monthlyRevenue);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}

