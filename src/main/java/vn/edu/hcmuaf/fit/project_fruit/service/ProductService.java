package vn.edu.hcmuaf.fit.project_fruit.service;

import vn.edu.hcmuaf.fit.project_fruit.dao.ProductDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;

import java.util.List;

public class ProductService {
    ProductDao productDao = new ProductDao();
    public List<Product> getAll(){
        return productDao.getAll();
    }
    public Product getDetails(int id) {
        ProductDao productDao = new ProductDao();
        return productDao.getById(id);
    }
    // Lấy sản phẩm theo danh mục
    public List<Product> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId); // Lấy sản phẩm theo danh mục
    }
    // Lấy sản phẩm theo khoảng id
    public List<Product> getProductsByIdRange(int startId, int endId) {
        return productDao.getProductsByIdRange(startId, endId);
    }
    // Thêm phương thức mới: Lấy sản phẩm ưu đãi trong tuần
    public List<Product> getWeeklyDiscountedProducts() {
        return productDao.getWeeklyDiscountedProducts();
    }
    public List<Product> searchProducts(String keyword) {
        return productDao.searchProductsByName(keyword);
    }
    public List<Product> getRelatedProducts(int categoryId, int excludeProductId) {
        return productDao.getRelatedProducts(categoryId, excludeProductId);
    }
    public List<Product> getBestSellingProducts() {
        return productDao.getBestSellingProducts();
    }
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        // Lấy sản phẩm bán chạy nhất
        List<Product> bestSellingProducts = productService.getBestSellingProducts();

        // Kiểm tra và in ra thông tin các sản phẩm bán chạy nhất
        if (bestSellingProducts != null && !bestSellingProducts.isEmpty()) {
            System.out.println("Sản phẩm bán chạy nhất:");
            for (Product product : bestSellingProducts) {
                System.out.println("Tên sản phẩm: " + product.getName());
                System.out.println("Tổng số lượng mua: " + product.getTotalQuantity());
                System.out.println("Tổng số tiền: " + product.getTotalAmount() + " VND");
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("Không có sản phẩm bán chạy nào.");
        }
    }

}

