package vn.edu.hcmuaf.fit.project_fruit.controller.notification;

import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/notifications")
public class AdminNotificationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Logs> logsList = new ArrayList<>();

        try (Connection conn = DbConnect.getConnection()) {
            LogsDao logsDao = new LogsDao(conn);
            logsList = logsDao.getUnseenLogs();

            // Đánh dấu tất cả log đã xem
           // logsDao.markAllLogsAsSeen();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(logsList);

        response.getWriter().write(json);
    }

}

