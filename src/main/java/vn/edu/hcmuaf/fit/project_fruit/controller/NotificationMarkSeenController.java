package vn.edu.hcmuaf.fit.project_fruit.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/notifications/mark-seen")
public class NotificationMarkSeenController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int userId = 0;
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userId = user.getId_account();
            }
        }

        if (userId > 0) {
            try (Connection conn = DbConnect.getConnection()) {
                LogsDao logsDao = new LogsDao(conn);
                logsDao.markAllLogsAsSeenByUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
