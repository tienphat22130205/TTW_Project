package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceDetailDao {
    public void addInvoiceDetail(int invoiceId, CartProduct item) {
        String sql = """
            INSERT INTO invoices_details (id_invoice, id_product, price, quantity, item_discount)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, invoiceId);
            ps.setInt(2, item.getId_product());
            ps.setDouble(3, item.getPrice());
            ps.setInt(4, item.getQuantity());
            ps.setDouble(5, item.getDiscount()); // đảm bảo đã có discount trong CartProduct
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi thêm invoice detail:");
            e.printStackTrace();
        }
    }

    // Main test
    public static void main(String[] args) {
        InvoiceDetailDao detailDao = new InvoiceDetailDao();

        CartProduct item = new CartProduct();
        item.setId_product(10); // id_product phải tồn tại
        item.setPrice(250000);
        item.setQuantity(2);
        item.setDiscount(50000); // mỗi sản phẩm giảm 50k (nếu áp dụng)

        detailDao.addInvoiceDetail(1, item); // ID đơn hàng giả định đã tồn tại
        System.out.println("✅ Đã thêm chi tiết đơn hàng cho invoiceId = 1");
    }
}
