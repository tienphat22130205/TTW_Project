package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/admin/invoice-detail")
public class InvoiceDetailServlet extends HttpServlet {
    private final InvoiceService service = new InvoiceService(); // ✅ dùng service

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int invoiceId;
        try {
            invoiceId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
            return;
        }

        List<CartProduct> details = service.getInvoiceDetails(invoiceId);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(details));
        out.flush();
    }
}
