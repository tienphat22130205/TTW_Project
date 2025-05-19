package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/admin/invoice-info")
public class InvoiceInfoServlet extends HttpServlet {
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

        Invoice invoice = service.getInvoiceById(invoiceId);
        if (invoice == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
            return;
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(invoice));
        out.flush();
    }
}
