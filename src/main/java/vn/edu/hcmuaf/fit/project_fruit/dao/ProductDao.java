package vn.edu.hcmuaf.fit.project_fruit.dao;

import vn.edu.hcmuaf.fit.project_fruit.dao.db.DbConnect;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.ProductImg;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.ProductList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // Lấy toàn bộ danh sách sản phẩm
    public List<Product> getAll() {
        try {
            ArrayList<Product> products = new ArrayList<>();
            String query = """
            SELECT p.*, pr.percent_discount
            FROM products p
            LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion;
            """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice();
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // Lấy sản phẩm theo danh mục
    public List<Product> getProductsByCategory(int categoryId) {
        try {
            ArrayList<Product> products = new ArrayList<>();
            String query = """
            SELECT p.*, pr.percent_discount 
            FROM products p 
            LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion 
            WHERE p.id_category = ?
            """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice();
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Product> getProductsByIdRange(int startId, int endId) {
        try {
            ArrayList<Product> products = new ArrayList<>();
            String query = """
            SELECT p.*, pr.percent_discount
            FROM products p
            LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
            WHERE p.id_product BETWEEN ? AND ?
            """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setInt(1, startId);
            ps.setInt(2, endId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice();
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // Lấy danh sách hình ảnh của sản phẩm từ bảng product_images
    private List<ProductImg> getImagesByProductId(int productId) {
        try {
            ArrayList<ProductImg> images = new ArrayList<>();
            String query = "SELECT * FROM product_images WHERE id_product = ?";
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductImg img = new ProductImg(
                        rs.getInt("id_image"),
                        rs.getString("url"),
                        rs.getBoolean("main_image")
                );
                if (img != null) {
                    images.add(img);
                    System.out.println("Found image: " + img.getUrl() + " for product ID: " + productId);
                }
            }
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Product> getWeeklyDiscountedProducts() {
        try {
            ArrayList<Product> products = new ArrayList<>();
            String query = """
                    SELECT p.*, pr.percent_discount
                    FROM products p
                    LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
                    WHERE pr.start_date <= NOW() AND pr.end_date >= NOW()\s
                    AND pr.type = 'weekly';
        """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy danh sách hình ảnh của sản phẩm
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));

                // Tạo đối tượng Product
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice(); // Tính giá sau giảm giá
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Product> searchProductsByName(String keyword) {
        try {
            ArrayList<Product> products = new ArrayList<>();
            String query = """
        SELECT p.*, pr.percent_discount
        FROM products p
        LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
        WHERE p.product_name LIKE ?;
        """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setString(1, "%" + keyword + "%"); // Tìm kiếm với từ khóa có chứa keyword
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice();
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public int getCategoryIdByProductId(int productId) {
        try {
            String query = "SELECT id_category FROM products WHERE id_product = ?";
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_category");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
    // Lấy sản phẩm theo ID
    public Product getById(int id) {
        try {
            String query = """
            SELECT p.*, pr.promotion_name, pr.percent_discount
            FROM products p
            LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
            WHERE p.id_product = ?
            """;
            PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                System.out.println("Product found: " + rs.getString("product_name"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getBoolean("status"),
                        rs.getString("describe_1"),
                        rs.getInt("quantity"),
                        rs.getString("origin"),
                        rs.getString("entry_date"),
                        rs.getString("shelf_life"),
                        rs.getString("warranty_period"),
                        rs.getString("characteristic"),
                        rs.getString("preserve_product"),
                        rs.getString("use_prodcut"),
                        rs.getString("benefit"),
                        rs.getString("promotion_name"),
                        rs.getDouble("percent_discount")
                );

                product.calculateDiscountedPrice();
                return product;
            } else {
                System.out.println("No product found in database for ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    public List<ProductList> getAllProducts() {
        List<ProductList> productList = new ArrayList<>();
        String query = """
                SELECT p.id_product, p.product_name, p.price, p.origin, p.status, c.name_category, p.describe_1, pi.url
                FROM products p
                JOIN category_products c ON p.id_category = c.id_category
                LEFT JOIN product_images pi ON p.id_product = pi.id_product
                ORDER BY p.id_product ASC
                """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductImg productImg = new ProductImg(0, rs.getString("url"), false);
                ProductList product = new ProductList(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        rs.getString("name_category"),
                        rs.getDouble("price"),
                        rs.getString("origin"),
                        rs.getBoolean("status"),
                        rs.getString("describe_1"),
                        productImg.getUrl()
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

   // Phương thức phân trang
    public List<ProductList> getProductsByPage(int page, int recordsPerPage) {
        List<ProductList> productList = new ArrayList<>();
        String query = """
                SELECT p.id_product, p.product_name, p.price, p.origin, p.status, c.name_category, p.describe_1, pi.url
                FROM products p
                JOIN category_products c ON p.id_category = c.id_category
                LEFT JOIN product_images pi ON p.id_product = pi.id_product
                ORDER BY p.id_product ASC
                LIMIT ?, ?
                """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ps.setInt(1, (page - 1) * recordsPerPage);  // Tính offset
            ps.setInt(2, recordsPerPage);  // Giới hạn số bản ghi mỗi trang
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductImg productImg = new ProductImg(0, rs.getString("url"), false);
                ProductList product = new ProductList(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        rs.getString("name_category"),
                        rs.getDouble("price"),
                        rs.getString("origin"),
                        rs.getBoolean("status"),
                        rs.getString("describe_1"),
                        productImg.getUrl()
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
    public List<Product> getProductDetailsByPage(int page, int recordsPerPage) {
        List<Product> productDetails = new ArrayList<>();
        String query = """
        SELECT p.*, pr.promotion_name, pr.percent_discount
        FROM products p
        LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
        ORDER BY p.id_product ASC
        LIMIT ? OFFSET ?
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ps.setInt(1, recordsPerPage);
            ps.setInt(2, (page - 1) * recordsPerPage);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));

                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getBoolean("status"),
                        rs.getString("describe_1"),
                        rs.getInt("quantity"),
                        rs.getString("origin"),
                        rs.getString("entry_date"),
                        rs.getString("shelf_life"),
                        rs.getString("warranty_period"),
                        rs.getString("characteristic"),
                        rs.getString("preserve_product"),
                        rs.getString("use_prodcut"),
                        rs.getString("benefit"),
                        rs.getString("promotion_name"),
                        rs.getDouble("percent_discount")
                );

                product.calculateDiscountedPrice();
                productDetails.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails;
    }
    // Lấy tổng số sản phẩm để tính số trang
    public int getTotalRecords() {
        String query = "SELECT COUNT(*) FROM products";
        int totalRecords = 0;
        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);  // Lấy giá trị COUNT(*) từ kết quả
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRecords;
    }
    public List<Product> getBestSellingProducts() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT p.product_name, SUM(id.quantity) AS total_quantity, SUM(id.quantity * p.price) AS total_amount " +
                "FROM invoices_details id " +
                "JOIN products p ON id.id_product = p.id_product " +
                "JOIN invoices i ON id.id_invoice = i.id_invoice " +
                "WHERE i.status = 'Đã hoàn thành' " +
                "GROUP BY p.product_name " +
                "ORDER BY total_quantity DESC " +
                "LIMIT 10";  // Lấy 10 sản phẩm bán chạy nhất

        try (PreparedStatement ps = DbConnect.getPreparedStatement(query, true);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int totalQuantity = rs.getInt("total_quantity");
                double totalAmount = rs.getDouble("total_amount");

                Product product = new Product(productName, totalQuantity, totalAmount);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static boolean deleteProductById(String productId) throws SQLException {
        String query = "DELETE FROM products WHERE id_product = ?";

        // Sử dụng DbConnect để lấy kết nối
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, productId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // Trả về true nếu xóa thành công, ngược lại false
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ
        }
    }
    public List<Product> getRelatedProducts(int categoryId, int excludeProductId) {
        List<Product> relatedProducts = new ArrayList<>();
        String query = """
            SELECT p.*, pr.percent_discount
            FROM products p
            LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion
            WHERE p.id_category = ? AND p.id_product != ?
            LIMIT 10
            """;

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, excludeProductId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                List<ProductImg> listImg = getImagesByProductId(rs.getInt("id_product"));
                Product product = new Product(
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        listImg,
                        rs.getDouble("price"),
                        rs.getString("rating"),
                        rs.getDouble("percent_discount")
                );
                product.calculateDiscountedPrice();
                relatedProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatedProducts;
    }
    public boolean reduceProductStock(int productId, int quantity) {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id_product = ? AND quantity >= ?";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, false)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi trừ kho sản phẩm #" + productId);
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateProduct(Product product) {
        String sql = """
        UPDATE products SET
            product_name = ?,
            price = ?,
            origin = ?,
            quantity = ?,
            warranty_period = ?,
            entry_date = ?,         -- Nếu cần cập nhật ngày nhập (nếu không thì bỏ)
            shelf_life = ?,
            describe_1 = ?,
            rating = ?,
            characteristic = ?,
            preserve_product = ?,
            use_prodcut = ?,
            benefit = ?,
            status = ?
        WHERE id_product = ?
    """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, false)) {
            ps.setString(1, product.getName());           // product_name
            ps.setDouble(2, product.getPrice());           // price
            ps.setString(3, product.getOrigin());         // origin
            ps.setInt(4, product.getQuantity());          // quantity
            ps.setString(5, product.getWarranty_period()); // warranty_period
            ps.setString(6, product.getEntry_date());     // entry_date (nếu muốn cập nhật)
            ps.setString(7, product.getShelf_life());     // shelf_life
            ps.setString(8, product.getDescribe_1());     // describe_1
            ps.setString(9, product.getRating());         // rating
            ps.setString(10, product.getCharacteristic());// characteristic
            ps.setString(11, product.getPreserve_product());// preserve_product
            ps.setString(12, product.getUse_prodcut());    // use_prodcut
            ps.setString(13, product.getBenefit());       // benefit
            ps.setBoolean(14, product.isStatus());        // status
            ps.setInt(15, product.getId_product());       // WHERE id_product = ?

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Lấy tổng số sản phẩm
    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getProductsInStock() {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 1";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getTotalSoldProducts() {
        String sql = """
        SELECT SUM(id.quantity) AS total_sold
        FROM invoices_details id
        JOIN invoices i ON id.id_invoice = i.id_invoice
        WHERE i.status = 'Đã thanh toán'
          AND i.order_status IN ('Đang chuẩn bị đơn hàng', 'Đã giao', 'Hoàn tất')
        """;

        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int totalSold = rs.getInt("total_sold");
                if (rs.wasNull()) {
                    return 0;
                }
                return totalSold;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getAverageRating() {
        String sql = "SELECT AVG(CAST(rating AS DECIMAL(3,2))) FROM products WHERE rating IS NOT NULL AND rating != ''";
        try (PreparedStatement ps = DbConnect.getPreparedStatement(sql, true)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
// ========================== MAIN TEST ==========================

    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();

        // Tạo đối tượng Product mẫu để test cập nhật
        Product product = new Product();

        // Giả sử bạn có setter trong class Product, set đầy đủ thông tin cần cập nhật:
        product.setId_product(10);                // ID sản phẩm cần update (phải có trong DB)
        product.setName("Quà tết 2025");
        product.setPrice(1500000);
        product.setOrigin("Việt Nam");
        product.setQuantity(50);
        product.setWarranty_period("12 tháng");
        product.setEntry_date("2025-05-21");    // định dạng yyyy-MM-dd hoặc phù hợp DB
        product.setShelf_life("6 tháng");
        product.setDescribe_1("Mô tả cập nhật cho sản phẩm");
        product.setRating("4.5");
        product.setCharacteristic("Đặc điểm cập nhật");
        product.setPreserve_product("Bảo quản nơi khô ráo");
        product.setUse_prodcut("Sử dụng theo hướng dẫn");
        product.setBenefit("Lợi ích khi dùng sản phẩm");
        product.setStatus(true);                 // trạng thái sản phẩm

        boolean updated = productDao.updateProduct(product);

        if (updated) {
            System.out.println("✅ Cập nhật sản phẩm thành công!");
            // Có thể lấy lại sản phẩm vừa cập nhật để kiểm tra
            Product updatedProduct = productDao.getById(product.getId_product());
            if (updatedProduct != null) {
                System.out.println("Thông tin sản phẩm sau cập nhật:");
                System.out.println("Tên sản phẩm: " + updatedProduct.getName());
                System.out.println("Giá: " + updatedProduct.getPrice());
                System.out.println("Xuất xứ: " + updatedProduct.getOrigin());
                System.out.println("Số lượng: " + updatedProduct.getQuantity());
                System.out.println("Bảo hành: " + updatedProduct.getWarranty_period());
                System.out.println("Ngày nhập: " + updatedProduct.getEntry_date());
                System.out.println("Hạn sử dụng: " + updatedProduct.getShelf_life());
                System.out.println("Mô tả: " + updatedProduct.getDescribe_1());
                System.out.println("Đánh giá: " + updatedProduct.getRating());
                System.out.println("Đặc điểm: " + updatedProduct.getCharacteristic());
                System.out.println("Bảo quản: " + updatedProduct.getPreserve_product());
                System.out.println("Cách dùng: " + updatedProduct.getUse_prodcut());
                System.out.println("Lợi ích: " + updatedProduct.getBenefit());
                System.out.println("Trạng thái: " + (updatedProduct.isStatus() ? "Hoạt động" : "Ngừng hoạt động"));
            } else {
                System.out.println("Không thể lấy dữ liệu sản phẩm sau khi cập nhật!");
            }
        } else {
            System.out.println("❌ Cập nhật sản phẩm thất bại.");
        }
    }


}
