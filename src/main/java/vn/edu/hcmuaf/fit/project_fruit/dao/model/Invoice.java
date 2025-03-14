package vn.edu.hcmuaf.fit.project_fruit.dao.model;

import java.util.Date;

public class Invoice {
    private String orderCode;          // Mã đơn hàng
    private String customerName;       // Tên khách hàng
    private String address;            // Địa chỉ
    private Date orderDate;            // Ngày đặt hàng
    private String invoiceDetails;     // Chi tiết hóa đơn
    private String paymentMethod;      // Phương thức thanh toán
    private String status;             // Tình trạng đơn hàng

    // Getters và Setters cho tất cả các thuộc tính
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(String invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
