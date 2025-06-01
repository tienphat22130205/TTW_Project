package vn.edu.hcmuaf.fit.project_fruit.dao.db;

import java.sql.*;

public class DbConnect {
    private static Connection conn;

    // Tạo kết nối khi cần
    private static void makeConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://" + DbProperties.host() + ":" + DbProperties.port() + "/" +
                DbProperties.dbname() + "?" + DbProperties.option();

        System.out.println("🔗 Kết nối tới: " + url);
        conn = DriverManager.getConnection(url, DbProperties.username(), DbProperties.password());
    }

    // Trả về Statement
    public static Statement get() {
        try {
            if (conn == null || conn.isClosed()) {
                makeConnect();
            }
            return conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Trả về PreparedStatement
    public static PreparedStatement getPreparedStatement(String query, boolean returnGeneratedKeys) {
        try {
            if (conn == null || conn.isClosed()) {
                makeConnect();
            }
            if (returnGeneratedKeys) {
                return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            } else {
                return conn.prepareStatement(query);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Trả về Connection
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                makeConnect();
            }
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
