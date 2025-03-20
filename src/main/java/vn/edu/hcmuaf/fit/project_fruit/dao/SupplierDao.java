package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDao {

    // Lấy tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        String query = """
            SELECT s.id_supplier, s.name, s.address, s.email, s.phone_number, s.status, s.rating, c.name_category, c.id_category
            FROM suppliers s
            LEFT JOIN category_products c ON s.id_category = c.id_category
            ORDER BY s.id_supplier ASC
        """;

        List<Supplier> suppliers = new ArrayList<>();

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("id_supplier"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("status"),
                        rs.getDouble("rating"),
                        rs.getString("name_category"),
                        rs.getInt("id_category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    // Lấy nhà cung cấp theo trang
    public List<Supplier> getSuppliersByPage(int page, int recordsPerPage) {
        String query = """
            SELECT s.id_supplier, s.name, s.address, s.email, s.phone_number, s.status, s.rating, c.name_category, c.id_category
            FROM suppliers s
            LEFT JOIN category_products c ON s.id_category = c.id_category
            ORDER BY s.id_supplier ASC
            LIMIT ?, ?
        """;

        List<Supplier> suppliers = new ArrayList<>();
        int offset = (page - 1) * recordsPerPage;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setInt(1, offset);
            ps.setInt(2, recordsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(new Supplier(
                            rs.getInt("id_supplier"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getString("status"),
                            rs.getDouble("rating"),
                            rs.getString("name_category"),
                            rs.getInt("id_category")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }

    // Tổng số nhà cung cấp
    public int getTotalRecords() {
        String query = "SELECT COUNT(*) AS total FROM suppliers";
        int totalRecords = 0;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalRecords = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }

    // Thêm nhà cung cấp
    public boolean addSupplier(Supplier supplier) {
        String query = """
            INSERT INTO suppliers (name, address, email, phone_number, status, rating, id_category)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getAddress());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone_number());
            ps.setString(5, supplier.getStatus());
            ps.setDouble(6, supplier.getRating());
            ps.setInt(7, supplier.getId_category());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
