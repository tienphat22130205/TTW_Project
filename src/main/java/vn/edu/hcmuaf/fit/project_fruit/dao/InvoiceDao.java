package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao {

    // Lấy tất cả các đơn hàng và thông tin khách hàng
    public List<Invoice> getAllInvoices() {
        String query = "SELECT i.id_invoice, i.id_account, i.total_price, i.status, i.payment_method, i.create_date, i.shipping_fee, i.shipping_method, c.customer_name, c.address " +
                "FROM invoices i " +
                "JOIN customers c ON i.id_account = c.id_customer " +
                "ORDER BY i.create_date DESC";

        List<Invoice> invoiceList = new ArrayList<>();

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query);
             ResultSet rs = ps.executeQuery()) {

            // Lặp qua tất cả các bản ghi và thêm vào danh sách
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setOrderCode(rs.getString("id_invoice"));
                invoice.setCustomerName(rs.getString("customer_name"));
                invoice.setAddress(rs.getString("address"));
                invoice.setOrderDate(rs.getDate("create_date"));
                invoice.setInvoiceDetails(rs.getString("total_price")); // Assuming total_price is the invoice detail for this example
                invoice.setPaymentMethod(rs.getString("payment_method"));
                invoice.setStatus(rs.getString("status"));
                invoiceList.add(invoice); // Thêm đối tượng vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoiceList; // Trả về danh sách tất cả các đơn hàng
    }
    public List<Invoice> getInvoicesByPage(int page, int recordsPerPage) {
        List<Invoice> invoices = new ArrayList<>();
        String query = """
            SELECT i.id_invoice, i.id_account, i.total_price, i.status, i.payment_method, 
                   i.create_date, i.shipping_fee, i.shipping_method, 
                   c.customer_name, c.address
            FROM invoices i
            JOIN customers c ON i.id_account = c.id_customer
            ORDER BY i.create_date DESC
            LIMIT ?, ?
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setInt(1, (page - 1) * recordsPerPage); // Tính offset
            ps.setInt(2, recordsPerPage); // Giới hạn số bản ghi mỗi trang

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setOrderCode(rs.getString("id_invoice"));
                invoice.setCustomerName(rs.getString("customer_name"));
                invoice.setAddress(rs.getString("address"));
                invoice.setOrderDate(rs.getDate("create_date"));
                invoice.setInvoiceDetails(rs.getString("total_price")); // Thông tin chi tiết hóa đơn
                invoice.setPaymentMethod(rs.getString("payment_method"));
                invoice.setStatus(rs.getString("status"));
                invoices.add(invoice); // Thêm hóa đơn vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    // Lấy tổng số hóa đơn trong bảng
    public int getTotalRecords() {
        String query = "SELECT COUNT(*) AS total FROM invoices";
        int totalRecords = 0;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt("total"); // Lấy tổng số bản ghi từ kết quả
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }
    // Phương thức main để kiểm tra
    public static void main(String[] args) {
        InvoiceDao invoiceDao = new InvoiceDao();
        List<Invoice> invoices = invoiceDao.getAllInvoices();

        for (Invoice invoice : invoices) {
            System.out.println("Mã đơn hàng: " + invoice.getOrderCode());
            System.out.println("Tên khách hàng: " + invoice.getCustomerName());
            System.out.println("Địa chỉ: " + invoice.getAddress());
            System.out.println("Ngày đặt hàng: " + invoice.getOrderDate());
            System.out.println("Chi tiết hóa đơn: " + invoice.getInvoiceDetails());
            System.out.println("Phương thức thanh toán: " + invoice.getPaymentMethod());
            System.out.println("Tình trạng: " + invoice.getStatus());
            System.out.println("----------------------------");
        }
    }
}
