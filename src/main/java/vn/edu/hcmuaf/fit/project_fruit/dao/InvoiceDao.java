package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class InvoiceDao {
    public int addInvoice(Invoice invoice) {
        String sql = """
            INSERT INTO invoices (id_account, receiver_name, phone, email, address_full,
                                  payment_method, shipping_method, total_price, shipping_fee, status,order_status, create_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
        """;
        int generatedId = -1;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, invoice.getAccountId());
            ps.setString(2, invoice.getReceiverName());
            ps.setString(3, invoice.getPhone());
            ps.setString(4, invoice.getEmail());
            ps.setString(5, invoice.getAddressFull());
            ps.setString(6, invoice.getPaymentMethod());
            ps.setString(7, invoice.getShippingMethod());
            ps.setDouble(8, invoice.getTotalPrice());
            ps.setDouble(9, invoice.getShippingFee());
            ps.setString(10, invoice.getStatus());
            ps.setString(11, invoice.getOrderStatus());

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
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();

        String sql = """
            SELECT 
                i.id_invoice AS id,
                c.customer_name AS account_name,
                i.receiver_name,
                i.phone,
                i.email,
                i.payment_method,
                i.status,
                i.order_status,
                i.address_full,
                i.total_price,
                i.shipping_fee,
                i.create_date AS created_at
            FROM invoices i
            JOIN accounts a ON i.id_account = a.id_account
            JOIN customers c ON a.id_customer = c.id_customer
            ORDER BY i.create_date DESC
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setIdInvoice(rs.getInt("id"));
                invoice.setAccountName(rs.getString("account_name")); // tên người mua từ bảng customers
                invoice.setReceiverName(rs.getString("receiver_name"));
                invoice.setPhone(rs.getString("phone"));
                invoice.setEmail(rs.getString("email"));
                invoice.setPaymentMethod(rs.getString("payment_method"));
                invoice.setStatus(rs.getString("status"));
                invoice.setOrderStatus(rs.getString("order_status"));
                invoice.setShippingFee(rs.getDouble("shipping_fee"));
                invoice.setAddressFull(rs.getString("address_full"));
                invoice.setTotalPrice(rs.getDouble("total_price"));
                invoice.setCreateDate(rs.getTimestamp("created_at"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn danh sách invoices:");
            e.printStackTrace();
        }

        return invoices;
    }
    public static Invoice getInvoiceById(int id) {
        String sql = """
        SELECT 
            i.id_invoice, c.customer_name, i.receiver_name, i.phone, i.email,
            i.address_full, i.total_price,i.shipping_fee, i.payment_method, i.status,i.order_status, i.create_date
        FROM invoices i
        JOIN accounts a ON i.id_account = a.id_account
        JOIN customers c ON a.id_customer = c.id_customer
        WHERE i.id_invoice = ?
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setIdInvoice(id);
                invoice.setAccountName(rs.getString("customer_name")); // tài khoản
                invoice.setReceiverName(rs.getString("receiver_name")); // người nhận
                invoice.setPhone(rs.getString("phone"));
                invoice.setEmail(rs.getString("email"));
                invoice.setAddressFull(rs.getString("address_full"));
                invoice.setTotalPrice(rs.getDouble("total_price"));
                invoice.setShippingFee(rs.getDouble("shipping_fee"));
                invoice.setPaymentMethod(rs.getString("payment_method"));
                invoice.setStatus(rs.getString("status"));
                invoice.setOrderStatus(rs.getString("order_status"));
                invoice.setCreateDate(rs.getTimestamp("create_date"));
                return invoice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean updateInvoiceStatus(int invoiceId, String status) {
        String sql = "UPDATE invoices SET status = ? WHERE id_invoice = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, false)) {
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi cập nhật trạng thái đơn hàng #" + invoiceId);
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateOrderStatus(int invoiceId, String orderStatus) {
        String sql = "UPDATE invoices SET order_status = ? WHERE id_invoice = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, false)) {
            ps.setString(1, orderStatus);
            ps.setInt(2, invoiceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi cập nhật order_status đơn hàng #" + invoiceId);
            e.printStackTrace();
            return false;
        }
    }
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM invoices";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getProcessingOrders() {
        String sql = "SELECT COUNT(*) FROM invoices WHERE order_status = 'Đang xử lý'";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // Lấy số đơn hàng đã thanh toán
    public int getPaidOrders() {
        String sql = "SELECT COUNT(*) FROM invoices WHERE status = 'Đã thanh toán'";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // Lấy số đơn hàng đã hủy
    public int getCancelledOrders() {
        String sql = "SELECT COUNT(*) FROM invoices WHERE order_status = 'Đã hủy'";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public Map<Integer, Double> getMonthlyRevenue() {
        Map<Integer, Double> result = new LinkedHashMap<>();
        String sql = "SELECT MONTH(create_date) AS month, SUM(total_price) AS revenue " +
                "FROM invoices WHERE status = 'Đã thanh toán' " +
                "GROUP BY MONTH(create_date) ORDER BY MONTH(create_date)";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int month = rs.getInt("month");
                double revenue = rs.getDouble("revenue");
                result.put(month, revenue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public Map<String, Double> getTopProductRevenue() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
        SELECT p.product_name, SUM(d.price * d.quantity) AS revenue
        FROM invoices i
        JOIN invoices_details d ON i.id_invoice = d.id_invoice
        JOIN products p ON p.id_product = d.id_product
        WHERE i.status = 'Đã thanh toán'
        GROUP BY p.product_name
        ORDER BY revenue DESC
        LIMIT 5
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("product_name"), rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public Map<String, Double> getRevenueByPaymentMethod() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
        SELECT payment_method, SUM(total_price) AS revenue
        FROM invoices
        WHERE status = 'Đã thanh toán'
        GROUP BY payment_method
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("payment_method"), rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public Map<String, Integer> getOrderStatusCountThisMonth() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = """
        SELECT status, COUNT(*) AS total
        FROM invoices
        WHERE MONTH(create_date) = MONTH(CURRENT_DATE)
          AND YEAR(create_date) = YEAR(CURRENT_DATE)
        GROUP BY status
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("status"), rs.getInt("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public Map<String, Object> getDashboardSummaryThisMonth() {
        Map<String, Object> result = new LinkedHashMap<>();
        String sql = """
        SELECT
          SUM(CASE WHEN status = 'Đã thanh toán' THEN total_price ELSE 0 END) AS total_revenue,
          COUNT(*) AS total_orders,
          SUM(CASE WHEN status = 'Đã hủy' THEN 1 ELSE 0 END) AS canceled_orders
        FROM invoices
        WHERE MONTH(create_date) = MONTH(CURDATE())
          AND YEAR(create_date) = YEAR(CURDATE())
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result.put("totalRevenue", rs.getDouble("total_revenue"));
                result.put("totalOrders", rs.getInt("total_orders"));
                result.put("canceledOrders", rs.getInt("canceled_orders"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<Map<String, Object>> getTopSpendingCustomers(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
        SELECT 
            c.customer_name AS fullname,
            c.customer_phone,
            c.address,
            SUM(i.total_price) AS total_spent
        FROM invoices i
        JOIN accounts a ON i.id_account = a.id_account
        JOIN customers c ON a.id_customer = c.id_customer
        WHERE i.status = 'Đã thanh toán'
        GROUP BY c.id_customer
        ORDER BY total_spent DESC
        LIMIT ?
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("fullname", rs.getString("fullname"));
                row.put("phone", rs.getString("customer_phone"));
                row.put("address", rs.getString("address"));
                row.put("totalSpent", rs.getDouble("total_spent"));
                result.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public Double getRevenueGrowthPercent() {
        String sql = """
        SELECT
            SUM(CASE WHEN MONTH(create_date) = MONTH(CURDATE()) AND YEAR(create_date) = YEAR(CURDATE()) THEN total_price ELSE 0 END) AS current_month,
            SUM(CASE WHEN MONTH(create_date) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND YEAR(create_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) THEN total_price ELSE 0 END) AS last_month
        FROM invoices
        WHERE status = 'Đã thanh toán'
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double current = rs.getDouble("current_month");
                double last = rs.getDouble("last_month");

                if (last == 0) return null; // Tránh chia cho 0

                return ((current - last) / last) * 100;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void main(String[] args) {
        InvoiceDao dao = new InvoiceDao();
        List<Map<String, Object>> list = dao.getTopSpendingCustomers(5);
        System.out.println("TEST DAO RESULT:");
        list.forEach(System.out::println);
    }
}
