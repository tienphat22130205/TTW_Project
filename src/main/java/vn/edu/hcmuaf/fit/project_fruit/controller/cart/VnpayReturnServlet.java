package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;

import java.io.IOException;
import java.util.Map;

@WebServlet("/vnpay-return")
public class VnpayReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String invoiceId = request.getParameter("vnp_TxnRef");

        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            new InvoiceDao().updateInvoiceStatus(Integer.parseInt(invoiceId), "Đã thanh toán");
            request.setAttribute("orderSuccess", true);
            request.setAttribute("invoiceId", invoiceId);
        } else {
            request.setAttribute("error", "Thanh toán thất bại, mã lỗi: " + responseCode);
        }

        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
    }
}


