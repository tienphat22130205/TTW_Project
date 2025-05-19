package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;

import java.io.IOException;

@WebServlet("/admin/approve-order")
public class ApproveOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String action = req.getParameter("action"); // "approve" hoặc "cancel"

        resp.setContentType("application/json");

        if (idStr == null || !idStr.matches("\\d+")) {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Thiếu ID\"}");
            return;
        }

        int id = Integer.parseInt(idStr);
        InvoiceDao dao = new InvoiceDao();

        boolean statusUpdated = false;
        boolean orderStatusUpdated = false;

        switch (action) {
            case "approve" -> {
                statusUpdated = dao.updateInvoiceStatus(id, "Đã thanh toán");
                orderStatusUpdated = dao.updateOrderStatus(id, "Đang chuẩn bị đơn hàng");
            }
            case "cancel" -> {
                statusUpdated = dao.updateInvoiceStatus(id, "Đã hủy");
                orderStatusUpdated = dao.updateOrderStatus(id, "Đã hủy");
            }
            default -> {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Hành động không hợp lệ\"}");
                return;
            }
        }

        if (statusUpdated && orderStatusUpdated) {
            resp.getWriter().write("{\"status\":\"success\", \"newStatus\":\"" + action + "\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Cập nhật thất bại\"}");
        }
    }
}

