package vn.edu.hcmuaf.fit.project_fruit.controller;

import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/notifications")
public class NotificationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int userId = 0;
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userId = user.getId_account();
            }
        }

        List<Logs> logsList = new ArrayList<>();
        if (userId > 0) {
            try (Connection conn = DbConnect.getConnection()) {
                LogsDao logsDao = new LogsDao(conn);
                logsList = logsDao.getUnseenLogsByUserIdExcludeLoginLogout(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<String> afterDataList = new ArrayList<>();
        if (logsList != null) {
            for (Logs log : logsList) {
                afterDataList.add(log.getAfterData());
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(afterDataList);

        response.getWriter().write(json);
    }
}

