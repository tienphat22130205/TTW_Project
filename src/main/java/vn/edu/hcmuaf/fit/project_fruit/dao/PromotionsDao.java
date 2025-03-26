package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromotionsDao {
    public List<Promotions> getAll() {
        PreparedStatement ps = DbConnect.getPreparedStatement("SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type FROM promotions ORDER BY id_promotion ASC");

        if (ps == null) return new ArrayList<>();

        List<Promotions> promotionsList = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                Promotions promotion = new Promotions(
                        rs.getInt("id_promotion"),
                        rs.getString("promotion_name"),
                        rs.getString("describe_1"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("percent_discount"),
                        rs.getString("type")
                );
                promotionsList.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return promotionsList;
    }

    // Method to retrieve a promotion by ID
    public Promotions getById(int id) {
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type FROM promotions WHERE id_promotion = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Promotions(
                        rs.getInt("id_promotion"),
                        rs.getString("promotion_name"),
                        rs.getString("describe_1"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("percent_discount"),
                        rs.getString("type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deletePromotionById(String promotionId) throws SQLException {
        String query = "DELETE FROM promotions WHERE id_promotion = ?";
        try (PreparedStatement preparedStatement = DbConnect.getPreparedStatement(query)) {
            if (preparedStatement == null) {
                throw new SQLException("Không thể tạo PreparedStatement.");
            }
            preparedStatement.setString(1, promotionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updatePromotion(Promotions promotion) {
        String query = "UPDATE promotions SET promotion_name = ?, describe_1 = ?, start_date = ?, end_date = ?, percent_discount = ?, type = ? WHERE id_promotion = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, promotion.getPromotion_name());  // Lấy tên khuyến mãi
            ps.setString(2, promotion.getDescribe_1());      // Mô tả khuyến mãi
            ps.setString(3, promotion.getStart_date());      // Ngày bắt đầu
            ps.setString(4, promotion.getEnd_date());        // Ngày kết thúc
            ps.setDouble(5, promotion.getPercent_discount()); // Phần trăm giảm giá
            ps.setString(6, promotion.getType());            // Loại khuyến mãi
            ps.setInt(7, promotion.getId_promotion());       // ID khuyến mãi

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Promotions> getPromotionsByPage(int page, int recordsPerPage) {
        List<Promotions> promotionsList = new ArrayList<>();
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type " +
                "FROM promotions " +
                "ORDER BY id_promotion ASC " +
                "LIMIT ?, ?";  // Phân trang ở đây

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setInt(1, (page - 1) * recordsPerPage);  // Tính offset
            ps.setInt(2, recordsPerPage);  // Giới hạn số bản ghi mỗi trang

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                promotionsList.add(new Promotions(
                        rs.getInt("id_promotion"),
                        rs.getString("promotion_name"),
                        rs.getString("describe_1"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("percent_discount"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotionsList;
    }

    // Phương thức lấy tổng số bản ghi để tính số trang
    public int getTotalRecords() {
        String query = "SELECT COUNT(*) FROM promotions";
        int totalRecords = 0;
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);  // Trả về số bản ghi
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }
    // Lấy khuyến mãi theo mã giảm giá
    public Promotions getPromotionByCode(String code) {
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type FROM promotions WHERE promotion_name = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Promotions(
                        rs.getInt("id_promotion"),
                        rs.getString("promotion_name"),
                        rs.getString("describe_1"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("percent_discount"),
                        rs.getString("type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        PromotionsDao dao = new PromotionsDao();

        // Gọi phương thức getAll()
        List<Promotions> promotionsList = dao.getAll();

        // Kiểm tra danh sách có dữ liệu không
        if (promotionsList.isEmpty()) {
            System.out.println("Không có khuyến mãi nào trong cơ sở dữ liệu.");
        } else {
            System.out.println("Danh sách khuyến mãi:");
            for (Promotions promotion : promotionsList) {
                System.out.println("ID: " + promotion.getId_promotion() +
                        ", Name: " + promotion.getPromotion_name() +
                        ", Description: " + promotion.getDescribe_1() +
                        ", Start Date: " + promotion.getStart_date() +
                        ", End Date: " + promotion.getEnd_date() +
                        ", Discount: " + promotion.getPercent_discount() +
                        ", Type: " + promotion.getType());
            }
        }
    }

}
