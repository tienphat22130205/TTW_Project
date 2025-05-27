// ✅ Servlet xử lý IPN từ VNPAY (gọi từ server VNPAY sang backend)
package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;
import vn.edu.hcmuaf.fit.project_fruit.utils.VnpayUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/project_fruit/ipn-vnpay")
public class IpnVnpayServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = new TreeMap<>();
        String receivedHash = req.getParameter("vnp_SecureHash");
        for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
            String key = e.nextElement();
            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
                params.put(key, req.getParameter(key));
            }
        }
        String rawData = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        String expectedHash = VnpayUtils.hmacSHA256("9X0IXG2TROOV7WE3FKQLBAI184BMC88A", rawData);

        if (!expectedHash.equalsIgnoreCase(receivedHash)) {
            resp.getWriter().write("INVALID HASH");
            return;
        }

        if ("00".equals(req.getParameter("vnp_ResponseCode"))) {
            // TODO: update đơn hàng
            resp.getWriter().write("SUCCESS IPN");
        } else {
            resp.getWriter().write("FAILED IPN");
        }
    }
}


