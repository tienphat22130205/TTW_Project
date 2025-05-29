package vn.edu.hcmuaf.fit.project_fruit.service;

import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDetailDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.util.List;

public class InvoiceService {
    private final InvoiceDao invoiceDao = new InvoiceDao();
    private final InvoiceDetailDao detailDao = new InvoiceDetailDao();

    public int createInvoice(User user, String name, String phone, String email, String address,
                             String paymentMethod, String shippingMethod,
                             double totalPrice, double shippingFee, String status, Cart cart) {

        Invoice invoice = new Invoice();
        invoice.setAccountId(user.getId_account());
        invoice.setReceiverName(name);
        invoice.setPhone(phone);
        invoice.setEmail(email);
        invoice.setAddressFull(address);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setShippingMethod(shippingMethod);
        invoice.setTotalPrice(totalPrice);
        invoice.setShippingFee(shippingFee);
        invoice.setStatus(status); // ✅ gán theo loại thanh toán
        invoice.setOrderStatus("Đang xử lý");

        int invoiceId = invoiceDao.addInvoice(invoice);

        if (invoiceId > 0) {
            for (CartProduct item : cart.getList()) {
                detailDao.addInvoiceDetail(invoiceId, item);
            }
        }

        return invoiceId;
    }
    public List<Invoice> getAllInvoices() {
        return invoiceDao.getAllInvoices();
    }
    public Invoice getInvoiceById(int id) {
        return InvoiceDao.getInvoiceById(id);
    }

    public List<CartProduct> getInvoiceDetails(int invoiceId) {
        return InvoiceDetailDao.getInvoiceDetails(invoiceId);
    }
    // 5️⃣ In chi tiết đơn hàng ra console (cho test/debug)
    public void printInvoiceDetail(int invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println("❌ Không tìm thấy đơn hàng #" + invoiceId);
            return;
        }

        System.out.println("🧾 CHI TIẾT ĐƠN HÀNG #" + invoice.getIdInvoice());
        System.out.println("👤 Người đặt: " + invoice.getAccountName());
        System.out.println("📞 SĐT: " + invoice.getPhone());
        System.out.println("📧 Email: " + invoice.getEmail());
        System.out.println("📍 Địa chỉ: " + invoice.getAddressFull());
        System.out.println("🚚 Phương thức thanh toán: " + invoice.getPaymentMethod());
        System.out.println("📦 Trạng thái: " + invoice.getStatus());
        System.out.println("📅 Ngày tạo: " + invoice.getCreateDate());
        System.out.println("🚛 Phí vận chuyển: " + invoice.getShippingFee() + " đ");
        System.out.println("💰 Tổng thanh toán: " + invoice.getTotalPrice() + " đ");

        List<CartProduct> products = getInvoiceDetails(invoiceId);
        if (products.isEmpty()) {
            System.out.println("⚠️ Không có sản phẩm nào.");
        } else {
            System.out.println("\n🛒 DANH SÁCH SẢN PHẨM:");
            int index = 1;
            for (CartProduct p : products) {
                double subtotal = p.getPrice() * p.getQuantity() * (1 - p.getDiscount() / 100);
                System.out.println("---------------");
                System.out.println("🔢 #" + index++);
                System.out.println("🆔 ID sản phẩm: " + p.getId_product());
                System.out.println("📦 Tên sản phẩm: " + p.getName());
                System.out.println("🔢 Số lượng: " + p.getQuantity());
                System.out.println("💵 Giá: " + p.getPrice() + " đ");
                System.out.println("🔻 Giảm giá: " + p.getDiscount() + " %");
                System.out.println("🧮 Thành tiền: " + subtotal + " đ");
            }
        }
    }

    // Test nhanh từ service
    public static void main(String[] args) {
        InvoiceService service = new InvoiceService();
        service.printInvoiceDetail(24); // 👈 sửa ID đơn hàng cần test
    }
}
