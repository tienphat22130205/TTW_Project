    package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

    import com.google.gson.Gson;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import vn.edu.hcmuaf.fit.project_fruit.dao.ProductDao;
    import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;
    import vn.edu.hcmuaf.fit.project_fruit.service.ProductService;

    import java.io.IOException;

    @WebServlet(name = "AdminProductDetailServlet", urlPatterns = "/admin/product-detail")
    public class AdminProductDetailServlet extends HttpServlet {

        private ProductDao productDao = new ProductDao();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String pidStr = request.getParameter("pid");

            if (pidStr == null || pidStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
                return;
            }

            try {
                int pid = Integer.parseInt(pidStr);
                Product product = productDao.getById(pid);

                if (product == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }

                Gson gson = new Gson();
                String productJson = gson.toJson(product);

                request.setAttribute("productJson", productJson);
                request.getRequestDispatcher("/admin/productDetail.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
            }
        }
    }
