package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;

import java.io.IOException;

@WebServlet("/vnpay-return")
public class VnpayReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String responseCode = req.getParameter("vnp_ResponseCode");
        String transactionStatus = req.getParameter("vnp_TransactionStatus");
        String txnRef = req.getParameter("vnp_TxnRef"); // chính là id_invoice

        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            int invoiceId = Integer.parseInt(txnRef);
            InvoiceDao dao = new InvoiceDao();
            dao.updateInvoiceStatus(invoiceId, "Đã thanh toán");
            dao.updateOrderStatus(invoiceId, "Đang chuẩn bị đơn hàng");
        }

        // Redirect về trang cảm ơn hoặc home
        resp.sendRedirect(req.getContextPath() + "/user/vnpay_return.jsp?vnp_TxnRef=" + txnRef +
                "&vnp_ResponseCode=" + responseCode +
                "&vnp_TransactionStatus=" + transactionStatus);
    }
}

