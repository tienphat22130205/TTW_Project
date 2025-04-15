package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.ShippingMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingMethodDAO {
    public List<ShippingMethod> getAllShippingMethods() {
        List<ShippingMethod> shippingMethods = new ArrayList<>();
        String query = "SELECT * FROM shipping_methods";
        try (Connection conn = DbConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShippingMethod method = new ShippingMethod(
                        rs.getInt("id"),
                        rs.getString("method_name"),
                        rs.getString("carrier"),
                        rs.getDouble("shipping_fee")
                );
                shippingMethods.add(method);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingMethods;
    }
}

