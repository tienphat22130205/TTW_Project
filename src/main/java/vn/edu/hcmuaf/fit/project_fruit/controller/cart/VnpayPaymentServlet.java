//package vn.edu.hcmuaf.fit.project_fruit.controller.cart;
//
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import vn.edu.hcmuaf.fit.project_fruit.utils.VnpayUtils;
//
//import java.io.IOException;
//
//@WebServlet("/vnpay-payment")
//public class VnpayPaymentServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String invoiceIdStr = req.getParameter("invoiceId");
//        String amountStr = req.getParameter("amount");
//        if (invoiceIdStr == null || amountStr == null) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu dữ liệu.");
//            return;
//        }
//
//        int invoiceId = Integer.parseInt(invoiceIdStr);
//        double amount = Double.parseDouble(amountStr);
//
//        String redirectUrl = VnpayUtils.buildPaymentUrl(invoiceId, amount);
//        resp.sendRedirect(redirectUrl);
//    }
//}
