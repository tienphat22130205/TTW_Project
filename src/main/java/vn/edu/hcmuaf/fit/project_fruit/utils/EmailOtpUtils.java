package vn.edu.hcmuaf.fit.project_fruit.utils;

public class EmailOtpUtils {
    /**
     * Gửi email OTP để xác minh đơn hàng
     */
    public static boolean sendOrderOtp(String toEmail, String otpCode) {
        String subject = "Mã xác thực đơn hàng - VitaminFruit";
        String content = "<h3>Xác minh đơn hàng của bạn</h3>"
                + "<p>Mã OTP của bạn là: <strong>" + otpCode + "</strong></p>"
                + "<p>Mã có hiệu lực trong 5 phút.</p>"
                + "<p>Nếu bạn không thực hiện đặt hàng, hãy bỏ qua email này.</p>";

        try {
            EmailUtils.sendEmail(toEmail, subject, content);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Gửi OTP đơn hàng thất bại: " + e.getMessage());
            return false;
        }
    }
}
