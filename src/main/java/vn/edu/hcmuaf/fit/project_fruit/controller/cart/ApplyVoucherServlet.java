package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;
import vn.edu.hcmuaf.fit.project_fruit.service.PromotionService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "ApplyVoucherServlet", value = "/apply-voucher")
public class ApplyVoucherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendRedirect("show-cart");
            return;
        }

        String voucherCode = request.getParameter("voucherCode");
        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            session.setAttribute("discountError", "Mã giảm giá không hợp lệ.");
            response.sendRedirect("show-cart");
            return;
        }

        PromotionService promotionService = new PromotionService();
        Promotions promotion = promotionService.getPromotionByCode(voucherCode.trim());

        if (promotion != null && promotion.getStart_date() != null && promotion.getEnd_date() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(promotion.getStart_date(), formatter);
            LocalDate endDate = LocalDate.parse(promotion.getEnd_date(), formatter);

            if (!LocalDate.now().isBefore(startDate) && !LocalDate.now().isAfter(endDate)) {
                double discount = (promotion.getPercent_discount() / 100.0) * cart.getTotalPrice();
                double newTotalPrice = cart.getTotalPrice() - discount;

                session.setAttribute("discount", discount);
                session.setAttribute("newTotalPrice", newTotalPrice);
                session.setAttribute("discountSuccess", "Áp dụng mã giảm giá thành công!");
            } else {
                session.setAttribute("discountError", "Mã giảm giá đã hết hạn.");
            }
        } else {
            session.setAttribute("discountError", "Mã giảm giá không hợp lệ.");
        }

        response.sendRedirect("show-cart");
    }
}