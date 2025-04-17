package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao {

    public int addInvoice(Invoice invoice) {
        String sql = """
        INSERT INTO invoices (id_account, receiver_name, phone, email, address_full,\s
                                         payment_method, shipping_method, total_price, shipping_fee, status, create_date)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
    """;
        int generatedId = -1;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, invoice.getAccountId());
            ps.setString(2, invoice.getReceiverName());
            ps.setString(3, invoice.getPhone());
            ps.setString(4, invoice.getEmail());
            ps.setString(5, invoice.getAddressFull());
            ps.setString(6, invoice.getPaymentMethod());
            ps.setString(7, invoice.getShippingMethod()); // shipping_method
            ps.setDouble(8, invoice.getTotalPrice());     // total_price
            ps.setDouble(9, invoice.getShippingFee());
            ps.setString(10, invoice.getStatus());


            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi thêm invoice:");
            e.printStackTrace();
        }

        return generatedId;
    }
    public static void main(String[] args) {
        InvoiceDao dao = new InvoiceDao();

        Invoice invoice = new Invoice();
        invoice.setAccountId(2); // Đảm bảo tồn tại
        invoice.setReceiverName("Test A");
        invoice.setPhone("0901234567");
        invoice.setEmail("a@test.com");
        invoice.setAddressFull("HCM");
        invoice.setPaymentMethod("COD");
        invoice.setTotalPrice(100000);
        invoice.setShippingFee(0);
        invoice.setShippingMethod("Giao hàng tiêu chuẩn"); // hoặc Giao nhanh, GHTK, v.v.
        invoice.setStatus("Chưa thanh toán");

        int id = dao.addInvoice(invoice);
        System.out.println("Kết quả thêm: " + id);
    }



}
