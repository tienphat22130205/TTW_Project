package vn.edu.hcmuaf.fit.project_fruit.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.LogsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Customer;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Logs;
import vn.edu.hcmuaf.fit.project_fruit.service.CustomerService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "UpdateCustomerController", value = "/update-customer-info")
public class UpdateCustomerController extends HttpServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customer currentCustomer = (Customer) request.getSession().getAttribute("customer");
        if (currentCustomer == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy thông tin trước khi update (dữ liệu cũ)
        int customerId = currentCustomer.getIdCustomer();
        Customer beforeUpdate = customerService.getCustomerById(customerId);

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String address = request.getParameter("address");

        boolean isUpdated = customerService.updateCustomerDetails(customerId, customerName, customerPhone, address);

        if (isUpdated) {
            // Lấy lại dữ liệu sau khi update (dữ liệu mới)
            Customer afterUpdate = customerService.getCustomerById(customerId);

            // Ghi log cập nhật
            try (Connection conn = DbConnect.getConnection()) {
                LogsDao logsDao = new LogsDao(conn);

                // Tạo dữ liệu before/after dạng chuỗi JSON (hoặc tự định dạng)
                String beforeData = String.format("Name: %s, Phone: %s, Address: %s",
                        beforeUpdate.getCustomerName(), beforeUpdate.getCustomerPhone(), beforeUpdate.getAddress());

                String afterData = String.format("Name: %s, Phone: %s, Address: %s",
                        afterUpdate.getCustomerName(), afterUpdate.getCustomerPhone(), afterUpdate.getAddress());

                Logs log = new Logs();
                log.setUserId(customerId);
                log.setLevel("INFO");
                log.setAction("UPDATE");
                log.setResource("Customer");
                log.setBeforeData(beforeData);
                log.setAfterData(afterData);
                log.setRole("User");

                logsDao.insertLog(log);

            } catch (SQLException e) {
                e.printStackTrace();
                // Có thể log lỗi này ra file hoặc xử lý phù hợp
            }

            // Cập nhật lại session
            request.getSession().setAttribute("customer", afterUpdate);
            request.setAttribute("successMessage", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("errorMessage", "Cập nhật thông tin thất bại. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("/user/user.jsp").forward(request, response);
    }
}
