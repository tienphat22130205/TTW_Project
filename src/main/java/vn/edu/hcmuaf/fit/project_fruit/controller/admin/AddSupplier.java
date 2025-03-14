//package vn.edu.hcmuaf.fit.project_fruit.controller.admin;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import vn.edu.hcmuaf.fit.project_fruit.dao.model.Supplier;
//import vn.edu.hcmuaf.fit.project_fruit.service.SupplierService;
//
//import java.io.IOException;
//
//@WebServlet(name = "AddSupplier", value = "/add-supplier")
//public class AddSupplier extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        SupplierService supplierService = new SupplierService();
//
//        try {
//            // Nhận các tham số từ form
//            String name = request.getParameter("name");
//            String address = request.getParameter("address");
//            String email = request.getParameter("email");
//            String phoneNumber = request.getParameter("phoneNumber");
//            String status = request.getParameter("status");
//            double rating = Double.parseDouble(request.getParameter("rating"));
//            int idCategory = Integer.parseInt(request.getParameter("idCategory"));
//
//            // Tạo đối tượng Supplier
//            Supplier supplier = new Supplier(name, address, email, phoneNumber, status, rating, idCategory);
//
//            // Thêm nhà cung cấp
//            boolean isAdded = supplierService.addSupplier(supplier);
//
//            if (isAdded) {
//                response.sendRedirect("admin#?message=success");
//            } else {
//                response.sendRedirect("admin#?message=error");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendRedirect("admin#?message=error");
//        }
//    }
//}
//
