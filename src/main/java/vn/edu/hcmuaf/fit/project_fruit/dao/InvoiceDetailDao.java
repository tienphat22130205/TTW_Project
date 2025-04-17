package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.CartItem;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceDetailDao {
    public void addInvoiceDetail(int invoiceId, CartItem item) {
        String sql = """
            INSERT INTO invoices_details (id_invoice, id_product, price, quantity, item_discount)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, invoiceId);
            ps.setInt(2, item.getProduct().getId_product());
            ps.setDouble(3, item.getProduct().getPrice());
            ps.setInt(4, item.getQuantity());
            ps.setDouble(5, item.getDiscount());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        InvoiceDetailDao detailDao = new InvoiceDetailDao();

        // Giả định invoiceId đã tồn tại (test với ID thật sau khi thêm)
        int invoiceId = 1;

        // Tạo sản phẩm giả lập
        Product product = new Product();
        product.setId_product(10);         // ID sản phẩm
        product.setPrice(250000);  // Giá

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setDiscount(50000); // mỗi sản phẩm giảm 50k

        detailDao.addInvoiceDetail(invoiceId, item);
        System.out.println("✅ Đã thêm chi tiết đơn hàng cho invoiceId = " + invoiceId);
    }

}
