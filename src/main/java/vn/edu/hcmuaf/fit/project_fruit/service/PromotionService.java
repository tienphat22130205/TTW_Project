package vn.edu.hcmuaf.fit.project_fruit.service;


import vn.edu.hcmuaf.fit.project_fruit.dao.PromotionsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;

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
        // Khởi tạo dịch vụ PromotionService
        PromotionService promotionService = new PromotionService();

        // Mã giảm giá cần kiểm tra
        String testVoucherCode = "TESTCODE"; // Thay bằng mã voucher bạn muốn kiểm tra

        // Thực hiện kiểm tra
        try {
            Promotions promotion = promotionService.getPromotionByCode(testVoucherCode);

            if (promotion != null) {
                System.out.println("Mã giảm giá hợp lệ!");
                System.out.println("Tên: " + promotion.getPromotion_name());
                System.out.println("Mô tả: " + promotion.getDescribe_1());
                System.out.println("Ngày bắt đầu: " + promotion.getStart_date());
                System.out.println("Ngày kết thúc: " + promotion.getEnd_date());
                System.out.println("Phần trăm giảm giá: " + promotion.getPercent_discount());
                System.out.println("Loại: " + promotion.getType());
            } else {
                System.out.println("Mã giảm giá không hợp lệ hoặc không tồn tại.");
            }
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
