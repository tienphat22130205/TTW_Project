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
        PreparedStatement ps = DbConnect.getPreparedStatement("SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type, code, min_order_amount, max_usage, usage_count FROM promotions ORDER BY id_promotion ASC");

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
                        rs.getString("type"),
                        rs.getString("code"),
                        rs.getDouble("min_order_amount"),
                        rs.getInt("max_usage"),
                        rs.getInt("usage_count")
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

    public Promotions getById(int id) {
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type, code, min_order_amount, max_usage, usage_count FROM promotions WHERE id_promotion = ?";
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
                        rs.getString("type"),
                        rs.getString("code"),
                        rs.getDouble("min_order_amount"),
                        rs.getInt("max_usage"),
                        rs.getInt("usage_count")
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
        String query = "UPDATE promotions SET promotion_name = ?, describe_1 = ?, start_date = ?, end_date = ?, percent_discount = ?, type = ?, code = ?, min_order_amount = ?, max_usage = ?, usage_count = ? WHERE id_promotion = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, promotion.getPromotion_name());
            ps.setString(2, promotion.getDescribe_1());
            ps.setString(3, promotion.getStart_date());
            ps.setString(4, promotion.getEnd_date());
            ps.setDouble(5, promotion.getPercent_discount());
            ps.setString(6, promotion.getType());
            ps.setString(7, promotion.getCode());
            ps.setDouble(8, promotion.getMin_order_amount());
            ps.setInt(9, promotion.getMax_usage());
            ps.setInt(10, promotion.getUsage_count());
            ps.setInt(11, promotion.getId_promotion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Promotions> getPromotionsByPage(int page, int recordsPerPage) {
        List<Promotions> promotionsList = new ArrayList<>();
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type, code, min_order_amount, max_usage, usage_count FROM promotions ORDER BY id_promotion ASC LIMIT ?, ?";

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setInt(1, (page - 1) * recordsPerPage);
            ps.setInt(2, recordsPerPage);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                promotionsList.add(new Promotions(
                        rs.getInt("id_promotion"),
                        rs.getString("promotion_name"),
                        rs.getString("describe_1"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("percent_discount"),
                        rs.getString("type"),
                        rs.getString("code"),
                        rs.getDouble("min_order_amount"),
                        rs.getInt("max_usage"),
                        rs.getInt("usage_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotionsList;
    }

    public int getTotalRecords() {
        String query = "SELECT COUNT(*) FROM promotions";
        int totalRecords = 0;
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    public Promotions getPromotionByCode(String code) {
        String query = "SELECT id_promotion, promotion_name, describe_1, start_date, end_date, percent_discount, type, code, min_order_amount, max_usage, usage_count FROM promotions WHERE code = ?";
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
                        rs.getString("type"),
                        rs.getString("code"),
                        rs.getDouble("min_order_amount"),
                        rs.getInt("max_usage"),
                        rs.getInt("usage_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        PromotionsDao dao = new PromotionsDao();
        List<Promotions> promotionsList = dao.getAll();
        if (promotionsList.isEmpty()) {
            System.out.println("Không có khuyến mãi nào trong cơ sở dữ liệu.");
        } else {
            System.out.println("Danh sách khuyến mãi:");
            for (Promotions promotion : promotionsList) {
                System.out.println(promotion);
            }
        }
    }
}
