package vn.edu.hcmuaf.fit.project_fruit.controller.admin;

import vn.edu.hcmuaf.fit.project_fruit.dao.PromotionsDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditPromotionServlet")
public class EditPromotion extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id_promotion"));
        String name = request.getParameter("promotion_name");
        String desc = request.getParameter("describe_1");
        String start = request.getParameter("start_date");
        String end = request.getParameter("end_date");
        double discount = Double.parseDouble(request.getParameter("percent_discount"));
        String type = request.getParameter("type");
        String code = request.getParameter("code");
        double minOrderAmount = Double.parseDouble(request.getParameter("min_order_amount"));
        int maxUsage = Integer.parseInt(request.getParameter("max_usage"));
        int usageCount = Integer.parseInt(request.getParameter("usage_count"));

        Promotions updatedPromo = new Promotions(id, name, desc, start, end, discount, type,
                code, minOrderAmount, maxUsage, usageCount);

        PromotionsDao dao = new PromotionsDao();
        boolean success = dao.updatePromotion(updatedPromo);

        if (success) {
            response.sendRedirect("admin?tab=promotions&update=success");
        } else {
            response.sendRedirect("admin?tab=promotions&update=fail");
        }
    }
}
