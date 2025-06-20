package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.*;
import vn.edu.hcmuaf.fit.project_fruit.service.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ListAdmin", value = "/admin")
public class ListAdmin extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FeedbackDao feedbackDao = new FeedbackDao();
        InvoiceDao invoiceDao = new InvoiceDao();
        CustomerService customerService = new CustomerService();
        ProductService productService = new ProductService();
        SupplierService supplierService = new SupplierService();

        // Lấy số trang từ request, mặc định là trang 1 nếu không có
        int page = 1;
        int recordsPerPage = 500;  // Số lượng feedback hiển thị mỗi trang
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Lấy danh sách feedback và phân trang
        List<Feedback> feedbacks = feedbackDao.getFeedbacksByPage(page, recordsPerPage);
        int noOfRecords = feedbackDao.getTotalRecords();  // Lấy tổng số bản ghi
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        // Đưa danh sách feedback vào request để hiển thị trong JSP
        request.setAttribute("feedback", feedbacks);  // Chỉ cần gọi một lần
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        //------------------------------------------------------------------------
        // Khách hàng gần đây
        List<Customer> recentCustomers = customerService.getRecentCustomers();
        request.setAttribute("recentCustomers", recentCustomers);

        // Kiểm tra xem có nhà cung cấp không
        int currentPage = 1;
        int itemsPerPage = 10; // Số lượng bản ghi hiển thị mỗi trang

        // Kiểm tra nếu có tham số "page" từ request để thay đổi trang hiện tại
        if (request.getParameter("page") != null) {
            try {
                currentPage = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                currentPage = 1; // Nếu tham số "page" không hợp lệ, mặc định là trang 1
            }
        }

        // Gọi service để lấy danh sách nhà cung cấp theo phân trang
        List<Supplier> supplierList = supplierService.getSuppliersByPage(currentPage, itemsPerPage);

        // Tính tổng số bản ghi và số trang
        int totalSupplierRecords = supplierService.getTotalRecords();
        int totalSupplierPages = (int) Math.ceil(totalSupplierRecords * 1.0 / itemsPerPage);

        // Đưa các giá trị vào request để chuyển sang JSP
        request.setAttribute("suppliers", supplierList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalSupplierPages);

        // --------------------------------------------------------------------------------------
        // Kiểm tra xem có khách hàng không
        CustomerDao customerDao = new CustomerDao();
        int customerPage = 1;
        if (request.getParameter("customerPage") != null) {
            customerPage = Integer.parseInt(request.getParameter("customerPage"));
        }

        int recordsPerPageCustomers = 200;  // Số lượng khách hàng hiển thị mỗi trang
//        List<Customer> customers = customerDao.getCustomersByPage(customerPage, recordsPerPageCustomers);
        int totalCustomers = customerDao.getTotalRecords();  // Lấy tổng số khách hàng
        int customerPages = (int) Math.ceil(totalCustomers * 1.0 / recordsPerPageCustomers);
        int totalCustomers1 = customerService.getTotalCustomers();
        List<String> userRoles = Arrays.asList("user");
        List<String> AdminStaffRoles = Arrays.asList("admin", "staff");

        List<Customer> customers = customerDao.getCustomersByRoles(userRoles);
        List<Customer> AdminStaff = customerDao.getCustomersByRoles(AdminStaffRoles);


        // Đưa danh sách khách hàng vào request để hiển thị trong JSP
        request.setAttribute("customersUser", customers);
        request.setAttribute("AdminStaff", AdminStaff);
        // Đưa danh sách khách hàng vào request để hiển thị trong JSP
//        request.setAttribute("customers", customers);
        request.setAttribute("customerPages", customerPages);
        request.setAttribute("currentCustomerPage", customerPage);
        request.setAttribute("totalCustomers1", totalCustomers1);

        // Kiểm tra xem có khách hàng không
        if (customers.isEmpty()) {
            request.setAttribute("customerMessage", "Không có khách hàng nào.");
        }
        //-------------------------------------------------------------------
        ProductDao productDao = new ProductDao();
        int productPage = 1;
        // Kiểm tra nếu có thông số "productPage" trong request (dùng để phân trang)
        if (request.getParameter("productPage") != null) {
            productPage = Integer.parseInt(request.getParameter("productPage"));
        }

        int recordsPerPageProducts = 150;
        // Số lượng sản phẩm hiển thị mỗi trang
        List<ProductList> products = productDao.getProductsByPage(productPage, recordsPerPageProducts);

        List<Product> productDetails = productDao.getProductDetailsByPage(productPage, recordsPerPageProducts);

        // Lấy tổng số sản phẩm
        int totalProducts = productDao.getTotalRecords();

        // Tính số trang cần thiết để hiển thị
        int productPages = (int) Math.ceil(totalProducts * 1.0 / recordsPerPageProducts);

        int totalProductsAdmin = productDao.getTotalProducts();
        int productsInStock = productDao.getProductsInStock();
        int totalSoldProducts = productDao.getTotalSoldProducts();
        double averageRating = productDao.getAverageRating();


        Gson gson = new Gson();
        Map<Integer, String> productJsonMap = productDetails.stream()
                .collect(Collectors.toMap(
                        Product::getId_product,
                        p -> gson.toJson(p)
                ));


        // Đưa danh sách sản phẩm vào request để hiển thị trong JSP
        request.setAttribute("products", products);
        request.setAttribute("productPages", productPages);
        request.setAttribute("currentProductPage", productPage);
        request.setAttribute("productJsonMap", productJsonMap);
        request.setAttribute("totalProductsAdmin", totalProductsAdmin);
        request.setAttribute("productsInStock", productsInStock);
        request.setAttribute("totalSoldProducts", totalSoldProducts);
        request.setAttribute("averageRating", String.format("%.1f", averageRating));

        // Kiểm tra nếu không có sản phẩm nào
        if (products.isEmpty()) {
            request.setAttribute("productMessage", "Không có sản phẩm nào.");
        }
//----------------------------------------------------------------------------------------------

        PromotionsDao promotionDao = new PromotionsDao();  // Tạo đối tượng PromotionDao để lấy dữ liệu khuyến mãi
        int promotionPage = 1;
        if (request.getParameter("promotionPage") != null) {
            promotionPage = Integer.parseInt(request.getParameter("promotionPage"));
        }

        int recordsPerPagePromotions = 10;  // Số lượng khuyến mãi hiển thị mỗi trang
        List<Promotions> promotions = promotionDao.getPromotionsByPage(promotionPage, recordsPerPagePromotions);

        // Lấy tổng số khuyến mãi
        int totalPromotions = promotionDao.getTotalRecords();

        // Tính số trang cần thiết để hiển thị khuyến mãi
        int promotionPages = (int) Math.ceil(totalPromotions * 1.0 / recordsPerPagePromotions);

        // Đưa danh sách khuyến mãi vào request để hiển thị trong JSP
        request.setAttribute("promotions", promotions);
        request.setAttribute("promotionPages", promotionPages);
        request.setAttribute("currentPromotionPage", promotionPage);

        // Kiểm tra nếu không có khuyến mãi nào
        if (promotions.isEmpty()) {
            request.setAttribute("promotionMessage", "Không có khuyến mãi nào.");
        }
        // Đơn hàng
        int pageInvoices = 1; // Trang hiện tại của danh sách hóa đơn
        int recordsPerPageInvoices = 20; // Số lượng hóa đơn hiển thị trên mỗi trang

        // Lấy số trang từ tham số request (nếu có)
        if (request.getParameter("InvoicesPage") != null) {
            pageInvoices = Integer.parseInt(request.getParameter("InvoicesPage"));
        }
        // -------------------------------------
        // Lấy danh sách đơn hàng (Invoices)
        InvoiceService invoiceService = new InvoiceService();
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<Invoice> newInvoices = invoiceService.getNewInvoices();
        int totalOrders = invoiceDao.getTotalOrders();
        int processingOrders = invoiceDao.getProcessingOrders();
        int paidOrders = invoiceDao.getPaidOrders();
        int cancelledOrders = invoiceDao.getCancelledOrders();
        Double growth = invoiceService.getRevenueGrowthPercent();
        List<Map<String, Object>> topCustomers = invoiceService.getTopSpendingCustomers(7);

        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("processingOrders", processingOrders);
        request.setAttribute("paidOrders", paidOrders);
        request.setAttribute("cancelledOrders", cancelledOrders);
        request.setAttribute("invoices", invoices);
        request.setAttribute("growthPercent", growth);
        request.setAttribute("topCustomers", topCustomers);
        request.setAttribute("newInvoices", newInvoices);

        // Chuyển tiếp tới JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/admin.jsp");
        dispatcher.forward(request, response);
    }


}
