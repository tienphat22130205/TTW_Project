package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDetailDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.ProductDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.CartProduct;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

                // Trừ số lượng tồn kho sản phẩm
                if (statusUpdated && orderStatusUpdated) {
                    InvoiceDetailDao detailDao = new InvoiceDetailDao();
                    List<CartProduct> items = detailDao.getInvoiceDetails(id);
                    ProductDao productDao = new ProductDao();

                    for (CartProduct item : items) {
                        productDao.reduceProductStock(item.getId_product(), item.getQuantity());
                    }
                }
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
            try (Connection conn = DbConnect.getConnection()) {
                LogsDao logsDao = new LogsDao(conn);
                Logs log = new Logs();

                // Giả sử bạn có session admin để lấy userId, role
                HttpSession session = req.getSession(false);
                int userId = 0;
                String role = "unknown";
                if (session != null && session.getAttribute("user") != null) {
                    vn.edu.hcmuaf.fit.project_fruit.dao.model.User user = (vn.edu.hcmuaf.fit.project_fruit.dao.model.User) session.getAttribute("user");
                    userId = user.getId_account();
                    role = user.getRole();
                }

                log.setUserId(userId);
                log.setLevel("INFO");
                log.setAction(action.equals("approve") ? "approve_order" : "cancel_order");
                log.setResource("invoice:" + id);
                log.setBeforeData("");  // Nếu có thể lấy trước trạng thái, ghi vào đây
                log.setAfterData("Đơn hàng #" + id + " đã " + (action.equals("approve") ? "được duyệt" : "bị hủy") +
                        ". Trạng thái hóa đơn: " + (action.equals("approve") ? "Đã thanh toán" : "Đã hủy") +
                        ", trạng thái đơn: " + (action.equals("approve") ? "Đang chuẩn bị đơn hàng" : "Đã hủy"));

                log.setRole(role);
                log.setSeen(false);

                logsDao.insertLog(log);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resp.getWriter().write("{\"status\":\"success\", \"newStatus\":\"" + action + "\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Cập nhật thất bại\"}");
        }
    }
}
