package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Customer;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Lấy thông tin người dùng qua email và mật khẩu
    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        String query = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Nếu tìm thấy người dùng, tạo đối tượng User
                user = new User(
                        rs.getInt("id_account"),
                        rs.getString("email"),
                        rs.getString("password"), // Mật khẩu hash
                        rs.getString("role"),
                        rs.getInt("id_customer")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    // Kiểm tra email đã tồn tại chưa
    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM accounts WHERE email = ?";
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Đăng ký người dùng vào bảng customers và accounts
    public boolean registerUser(User user, String fullName) {
        String insertCustomerQuery = "INSERT INTO customers (customer_name, customer_phone, address) VALUES (?, ?, ?)";
        String insertAccountQuery = "INSERT INTO accounts (email, password, role, create_date, id_customer) VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement psCustomer = null;
        PreparedStatement psAccount = null;

        try {
            connection = DbConnect.getConnection();
            connection.setAutoCommit(false); // Bắt đầu transaction

            // Thêm thông tin vào bảng customers
            psCustomer = connection.prepareStatement(insertCustomerQuery, Statement.RETURN_GENERATED_KEYS);
            psCustomer.setString(1, fullName);
            psCustomer.setString(2, "unknown"); // Giá trị mặc định cho customer_phone
            psCustomer.setString(3, "unknown"); // Address để NULL
            psCustomer.executeUpdate();

            // Lấy id_customer được tự động tạo
            ResultSet rs = psCustomer.getGeneratedKeys();
            int idCustomer = 0;
            if (rs.next()) {
                idCustomer = rs.getInt(1);
            }

            // Thêm thông tin vào bảng accounts
            psAccount = connection.prepareStatement(insertAccountQuery);
            psAccount.setString(1, user.getEmail());
            psAccount.setString(2, user.getPassword()); // Mật khẩu đã hash
            psAccount.setString(3, user.getRole()); // Vai trò (mặc định là "user")
            psAccount.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now())); // Ngày tạo
            psAccount.setInt(5, idCustomer); // Liên kết id_customer từ bảng customers
            psAccount.executeUpdate();

            connection.commit(); // Xác nhận transaction
            return true; // Đăng ký thành công

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(); // Khôi phục trạng thái ban đầu nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            try {
                if (psCustomer != null) psCustomer.close();
                if (psAccount != null) psAccount.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
        return false; // Đăng ký thất bại
    }

    // Cập nhật mật khẩu theo email
    public boolean updatePasswordByEmail(String email, String hashedPassword) {
        String query = "UPDATE accounts SET password = ? WHERE email = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public User getUserByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM accounts WHERE email = ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id_account"), rs.getString("email"), rs.getString("password"), rs.getString("role"), rs.getInt("id_customer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
