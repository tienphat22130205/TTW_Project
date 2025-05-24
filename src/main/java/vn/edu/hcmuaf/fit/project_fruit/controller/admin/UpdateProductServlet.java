package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
import vn.edu.hcmuaf.fit.project_fruit.service.ProductService;

import java.io.IOException;

@WebServlet(name = "UpdateProductServlet", urlPatterns = "/admin/update-product")
public class UpdateProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService(); // Khởi tạo service
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form gửi lên
            int idProduct = Integer.parseInt(request.getParameter("id_product"));
            String name = request.getParameter("product_name");
            double price = Double.parseDouble(request.getParameter("price"));
            String origin = request.getParameter("origin");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String warrantyPeriod = request.getParameter("warranty_period");
            String entryDate = request.getParameter("entry_date");
            String shelfLife = request.getParameter("shelf_life");
            String describe = request.getParameter("describe_1");
            String rating = request.getParameter("rating");
            String characteristic = request.getParameter("characteristic");
            String preserveProduct = request.getParameter("preserve_product");
            String useProduct = request.getParameter("use_prodcut");
            String benefit = request.getParameter("benefit");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            // Tạo đối tượng Product và set các trường
            Product product = new Product();
            product.setId_product(idProduct);
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

            // Gọi service cập nhật sản phẩm
            boolean success = productService.updateProduct(product);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/products?update=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products?update=fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?update=error");
        }
    }
}
