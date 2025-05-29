package vn.edu.hcmuaf.fit.project_fruit.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class VnpayUtils {
    public static String buildPaymentUrl(int invoiceId, double amount) {
        String vnpTxnRef = String.valueOf(invoiceId);
        String vnpOrderInfo = "Thanh toán đơn hàng #" + invoiceId;
        String orderType = "other";
        String vnpAmount = String.valueOf((long) (amount * 100)); // nhân 100 vì đơn vị là VND * 100

        String vnpIpAddr = "127.0.0.1"; // nếu có IP user thì lấy ở request

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String vnpCreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VnpayConfig.VNP_VERSION);
        vnpParams.put("vnp_Command", VnpayConfig.VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VnpayConfig.VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", vnpOrderInfo);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VnpayConfig.VNP_RETURN_URL);
        vnpParams.put("vnp_IpAddr", vnpIpAddr);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        // Sắp xếp và build query
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if ((value != null) && (value.length() > 0)) {
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                            .append('=')
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
                    hashData.append(fieldName).append('=').append(value);
                    if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        String secureHash = hmacSHA512(VnpayConfig.VNP_HASH_SECRET, hashData.toString());
        return VnpayConfig.VNP_PAY_URL + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    private static String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac hmac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKey);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo HMAC SHA512", e);
        }
    }
}
