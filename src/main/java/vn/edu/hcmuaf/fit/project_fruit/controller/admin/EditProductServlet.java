package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
import vn.edu.hcmuaf.fit.project_fruit.service.ProductService;

import java.io.IOException;

@WebServlet(name = "EditProductServlet", value = "/admin/edit-product")
public class EditProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String idStr = request.getParameter("id_product");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");

            if (idStr == null || idStr.isEmpty()
                    || priceStr == null || priceStr.isEmpty()
                    || quantityStr == null || quantityStr.isEmpty()) {
                throw new IllegalArgumentException("Thiếu dữ liệu bắt buộc");
            }

            int id = Integer.parseInt(idStr);
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            String name = request.getParameter("name");
            String origin = request.getParameter("origin");
            String warrantyPeriod = request.getParameter("warranty_period");
            String entryDate = request.getParameter("entry_date");
            String shelfLife = request.getParameter("shelf_life");
            String describe = request.getParameter("describe_1");
            String rating = request.getParameter("rating");
            String characteristic = request.getParameter("characteristic");
            String preserveProduct = request.getParameter("preserve_product");
            String useProduct = request.getParameter("use_product");
            String benefit = request.getParameter("benefit");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            Product product = new Product();
            product.setId_product(id);
            product.setName(name);
            product.setPrice(price);
            product.setOrigin(origin);
            product.setQuantity(quantity);
            product.setWarranty_period(warrantyPeriod);
            product.setEntry_date(entryDate);
            product.setShelf_life(shelfLife);
            product.setDescribe_1(describe);
            product.setRating(rating);
            product.setCharacteristic(characteristic);
            product.setPreserve_product(preserveProduct);
            product.setUse_prodcut(useProduct);
            product.setBenefit(benefit);
            product.setStatus(status);

            boolean success = productService.updateProduct(product);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Bạn đã chỉnh sửa thành công!\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Cập nhật sản phẩm thất bại.\"}");
            }

        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Giá trị số không hợp lệ.\"}");
        } catch (IllegalArgumentException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi hệ thống.\"}");
        }
    }
}
