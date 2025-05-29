//// ✅ Servlet xử lý IPN từ VNPAY (gọi từ server VNPAY sang backend)
//package vn.edu.hcmuaf.fit.project_fruit.controller.cart;
//
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;
//import vn.edu.hcmuaf.fit.project_fruit.utils.VnpayUtils;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@WebServlet("/project_fruit/ipn-vnpay")
//public class IpnVnpayServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Map<String, String> params = new TreeMap<>();
//        String receivedHash = req.getParameter("vnp_SecureHash");
//        for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
//            String key = e.nextElement();
//            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
//                params.put(key, req.getParameter(key));
//            }
//        }
//        String rawData = params.entrySet().stream()
//                .map(e -> e.getKey() + "=" + e.getValue())
//                .collect(Collectors.joining("&"));
//
//        // Dùng secret key giống VnpayUtils
//        String secretKey = "6IY1AS7Y98Q2EHOP24JRPDJQKCLJM3FS";
//        String expectedHash = VnpayUtils.hmacSHA256(secretKey, rawData);
//
//        if (!expectedHash.equalsIgnoreCase(receivedHash)) {
//            resp.getWriter().write("INVALID HASH");
//            return;
//        }
//
//        String responseCode = req.getParameter("vnp_ResponseCode");
//        String txnRef = req.getParameter("vnp_TxnRef");
//
//        if ("00".equals(responseCode)) {
//            // TODO: cập nhật trạng thái đơn hàng trong DB theo txnRef, ví dụ:
//            // InvoiceDao invoiceDao = new InvoiceDao();
//            // invoiceDao.updateStatus(txnRef, "PAID");
//            resp.getWriter().write("00"); // Trả về 00 cho VNPAY biết xử lý thành công
//        } else {
//            resp.getWriter().write("99"); // Trả về 99 hoặc code lỗi khác
//        }
//    }
//
//    // Nếu VNPAY gọi GET, cũng có thể override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        doPost(req, resp);
//    }
//}
//
//
//
