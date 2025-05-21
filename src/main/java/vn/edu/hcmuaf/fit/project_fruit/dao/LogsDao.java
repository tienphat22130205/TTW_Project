package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogsDao {
    private Connection conn;

    public LogsDao(Connection conn) {
        this.conn = conn;
    }

    public void insertLog(Logs log) throws SQLException {
        String sql = "INSERT INTO logs (user_id, level, action, resource, before_data, after_data, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, log.getUserId());
            stmt.setString(2, log.getLevel());
            stmt.setString(3, log.getAction());
            stmt.setString(4, log.getResource());
            stmt.setString(5, log.getBeforeData());
            stmt.setString(6, log.getAfterData());
            stmt.setString(7, log.getRole());
            stmt.executeUpdate();
        }
    }
}
