package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;
import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "ReceiptServlet", urlPatterns = "/receipt")
public class ReceiptServlet extends HttpServlet {

    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Lấy thông tin hóa đơn
        Invoice invoice = invoiceService.getInvoiceById(orderId);
        if (invoice == null) {
            // Không tìm thấy hóa đơn, chuyển về trang chủ hoặc trang lỗi
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Lấy chi tiết các sản phẩm trong hóa đơn
        List<CartProduct> invoiceDetails = invoiceService.getInvoiceDetails(orderId);

        // Đưa dữ liệu sang JSP
        request.setAttribute("invoice", invoice);
        request.setAttribute("invoiceDetails", invoiceDetails);

        request.getRequestDispatcher("/user/receipt.jsp").forward(request, response);
    }
}
