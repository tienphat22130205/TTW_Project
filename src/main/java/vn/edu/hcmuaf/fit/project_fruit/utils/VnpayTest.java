package vn.edu.hcmuaf.fit.project_fruit.utils;

public class VnpayTest {
    public static void main(String[] args) {
        int invoiceId = 123;           // giả lập mã hoá đơn
        double amount = 150000.0;      // giả lập tổng tiền

        String url = VnpayUtils.buildPaymentUrl(invoiceId, amount);
        System.out.println("🔗 Redirect to: " + url);
    }
}