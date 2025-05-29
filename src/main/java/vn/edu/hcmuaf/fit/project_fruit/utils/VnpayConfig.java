package vn.edu.hcmuaf.fit.project_fruit.utils;

public class VnpayConfig {
    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";
    public static final String VNP_TMN_CODE = "ZZ0LWKMA"; // mã website do VNPAY cấp
    public static final String VNP_HASH_SECRET = "6IY1AS7Y98Q2EHOP24JRPDJQKCLJM3FS"; // mã bí mật
    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_RETURN_URL = "http://localhost:8091/project_fruit/vnpay-return";
}
