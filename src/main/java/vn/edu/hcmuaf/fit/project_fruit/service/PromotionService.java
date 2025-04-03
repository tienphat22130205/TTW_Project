package vn.edu.hcmuaf.fit.project_fruit.service;


import vn.edu.hcmuaf.fit.project_fruit.dao.PromotionsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class PromotionService {

    private PromotionsDao promotionsDao;
    // Constructor
    public PromotionService() {
        this.promotionsDao = new PromotionsDao();  // Khởi tạo đối tượng PromotionsDao
    }

    // Lấy tất cả khuyến mãi
    public List<Promotions> getAllPromotions() {
        return promotionsDao.getAll();  // Gọi phương thức getAll() từ PromotionsDao
    }

    // Lấy khuyến mãi theo ID
    public Promotions getPromotionById(int id) {
        return promotionsDao.getById(id);  // Gọi phương thức getById() từ PromotionsDao
    }
    // Cập nhật khuyến mãi
    public boolean updatePromotion(Promotions promotion) {
        // Thêm logic nghiệp vụ nếu cần
        return promotionsDao.updatePromotion(promotion);  // Gọi phương thức updatePromotion() từ PromotionsDao
    }
    // Lấy khuyến mãi theo phân trang
    public List<Promotions> getPromotionsByPage(int page, int recordsPerPage) {
        return promotionsDao.getPromotionsByPage(page, recordsPerPage);  // Gọi phương thức phân trang từ PromotionsDao
    }

    // Lấy tổng số bản ghi trong bảng promotions
    public int getTotalRecords() {
        return promotionsDao.getTotalRecords();  // Gọi phương thức lấy tổng số bản ghi từ PromotionsDao
    }
    public Promotions getPromotionByCode(String code) {
        return promotionsDao.getPromotionByCode(code);
    }
    public static void main(String[] args) {
        // Mã giảm giá cần test
        String testCode = "FRUIT10"; // đổi thành mã có trong DB của bạn

        // Tổng tiền đơn hàng giả lập (test với cả dưới và trên min)
        double testTotal = 450000; // đổi thành 350000 để test mã hợp lệ

        // Khởi tạo service
        PromotionService service = new PromotionService();
        Promotions promotion = service.getPromotionByCode(testCode);

        if (promotion != null) {
            System.out.println("Tìm thấy mã giảm giá: " + promotion.getPromotion_name());

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate now = LocalDate.now();
                LocalDate startDate = LocalDate.parse(promotion.getStart_date(), formatter);
                LocalDate endDate = LocalDate.parse(promotion.getEnd_date(), formatter);

                if (now.isBefore(startDate) || now.isAfter(endDate)) {
                    System.out.println("❌ Mã giảm giá đã hết hạn hoặc chưa có hiệu lực.");
                    return;
                }

                if (testTotal < promotion.getMin_order_amount()) {
                    System.out.println("❌ Tổng đơn hàng không đủ điều kiện áp dụng. Cần >= " + promotion.getMin_order_amount());
                    return;
                }

                double discount = (promotion.getPercent_discount() / 100.0) * testTotal;
                double newTotal = testTotal - discount;

                System.out.println("✅ Mã hợp lệ. Giảm " + promotion.getPercent_discount() + "%");
                System.out.println("→ Giảm được: " + discount + "₫");
                System.out.println("→ Tổng sau giảm: " + newTotal + "₫");

            } catch (Exception e) {
                System.out.println("❌ Lỗi xử lý ngày: " + e.getMessage());
            }

        } else {
            System.out.println("❌ Không tìm thấy mã giảm giá.");
        }
    }

}
