package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogsDao {
    private Connection conn;

    public LogsDao(Connection conn) {
        this.conn = conn;
    }

    public void insertLog(Logs log) throws SQLException {
        String sql = "INSERT INTO logs (user_id, level, action, resource, before_data, after_data, role, seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, log.getUserId());
            stmt.setString(2, log.getLevel());
            stmt.setString(3, log.getAction());
            stmt.setString(4, log.getResource());
            stmt.setString(5, log.getBeforeData());
            stmt.setString(6, log.getAfterData());
            stmt.setString(7, log.getRole());
            stmt.setBoolean(8, log.isSeen());
            stmt.executeUpdate();
        }
    }

    public List<Logs> getAllLogs() {
        List<Logs> list = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Logs log = new Logs();
                log.setUserId(rs.getInt("user_id"));
                log.setLevel(rs.getString("level"));
                log.setAction(rs.getString("action"));
                log.setResource(rs.getString("resource"));
                log.setBeforeData(rs.getString("before_data"));
                log.setAfterData(rs.getString("after_data"));
                log.setRole(rs.getString("role"));
                log.setSeen(rs.getBoolean("seen"));
                list.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Logs> getLogsByUserIdExcludeLoginLogout(int userId) throws SQLException {
        List<Logs> logsList = new ArrayList<>();
        String sql = "SELECT * FROM logs WHERE user_id = ? AND action NOT IN ('login', 'logout') ORDER BY id DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setUserId(rs.getInt("user_id"));
                log.setLevel(rs.getString("level"));
                log.setAction(rs.getString("action"));
                log.setResource(rs.getString("resource"));
                log.setBeforeData(rs.getString("before_data"));
                log.setAfterData(rs.getString("after_data"));
                log.setRole(rs.getString("role"));
                log.setSeen(rs.getBoolean("seen"));
                logsList.add(log);
            }
        }
        return logsList;
    }


    public List<Logs> getUnseenLogsByUserIdExcludeLoginLogout(int userId) throws SQLException {
        String sql = "SELECT * FROM logs WHERE user_id = ? AND seen = FALSE AND action NOT IN ('login', 'logout', 'add_product') ORDER BY id DESC";
        List<Logs> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setUserId(rs.getInt("user_id"));
                log.setLevel(rs.getString("level"));
                log.setAction(rs.getString("action"));
                log.setResource(rs.getString("resource"));
                log.setBeforeData(rs.getString("before_data"));
                log.setAfterData(rs.getString("after_data"));
                log.setRole(rs.getString("role"));
                log.setSeen(rs.getBoolean("seen"));
                list.add(log);
            }
        }
        return list;
    }

    public List<Logs> getUnseenLogs() throws SQLException {
        String sql = "SELECT * FROM logs WHERE seen = FALSE ORDER BY id DESC";
        List<Logs> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Logs log = new Logs();
                log.setUserId(rs.getInt("user_id"));
                log.setLevel(rs.getString("level"));
                log.setAction(rs.getString("action"));
                log.setResource(rs.getString("resource"));
                log.setBeforeData(rs.getString("before_data"));
                log.setAfterData(rs.getString("after_data"));
                log.setRole(rs.getString("role"));
                log.setSeen(rs.getBoolean("seen"));
                list.add(log);
            }
        }
        return list;
    }



    public void markAllLogsAsSeenByUserId(int userId) throws SQLException {
        String sql = "UPDATE logs SET seen = TRUE WHERE user_id = ? AND seen = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
    public void markAllLogsAsSeen() throws SQLException {
        String sql = "UPDATE logs SET seen = TRUE WHERE seen = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }


    public static void main(String[] args) {
        try (Connection conn = DbConnect.getConnection()) {
            LogsDao dao = new LogsDao(conn);
            int userId = 79;
            List<Logs> logs = dao.getAllLogs();

            if (logs.isEmpty()) {
                System.out.println("Không có logs cho userId = " + userId);
            } else {
                for (Logs log : logs) {
                    System.out.println("Action: " + log.getAction() + ", Resource: " + log.getResource() +
                            ", Before: " + log.getBeforeData() + ", After: " + log.getAfterData() +
                            ", Level: " + log.getLevel() + ", Role: " + log.getRole());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
