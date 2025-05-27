package vn.edu.hcmuaf.fit.project_fruit.utils;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class VnpayUtils {
    public static String buildPaymentUrl(int invoiceId, double amount) {
        String vnp_TmnCode = "ZZ0LWKMA";
        String vnp_HashSecret = "9X0IXG2TROOV7WE3FKQLBAI184BMC88A";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "https://440a-2402-800-631d-7ae6-70fa-b579-fe66-ef72.ngrok-free.app/project_fruit/vnpay-return";
        String ipnUrl = "https://440a-2402-800-631d-7ae6-70fa-b579-fe66-ef72.ngrok-free.app/project_fruit/ipn-vnpay";

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((long) amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(invoiceId));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don " + invoiceId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpnUrl", ipnUrl);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // ✅ Tạo chuỗi dữ liệu để ký
        String rawData = vnp_Params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        // ✅ Sinh chữ ký đúng chuẩn
        String secureHash = hmacSHA256(vnp_HashSecret, rawData);
        vnp_Params.put("vnp_SecureHash", secureHash);
        vnp_Params.put("vnp_SecureHashType", "HmacSHA256");

        // ✅ Encode query
        return vnp_Url + "?" + vnp_Params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.US_ASCII) + "=" +
                        URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }

    public static String hmacSHA256(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}