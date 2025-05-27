    package vn.edu.hcmuaf.fit.project_fruit.controller.cart;

    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.*;
    import vn.edu.hcmuaf.fit.project_fruit.dao.ShippingMethodDAO;
    import vn.edu.hcmuaf.fit.project_fruit.dao.cart.Cart;
    import vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions;
    import vn.edu.hcmuaf.fit.project_fruit.dao.model.ShippingMethod;
    import vn.edu.hcmuaf.fit.project_fruit.dao.model.User;
    import vn.edu.hcmuaf.fit.project_fruit.service.InvoiceService;
    import vn.edu.hcmuaf.fit.project_fruit.service.PromotionService;
    import vn.edu.hcmuaf.fit.project_fruit.utils.VnpayUtils;

    import java.io.IOException;
    import java.util.List;

    @WebServlet(name = "CheckoutServlet", value = "/checkout")
    public class CheckoutServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || cart.getList().isEmpty()) {
                response.sendRedirect("show-cart");
                return;
            }

            PromotionService promotionService = new PromotionService();
            List<Promotions> promotionList = promotionService.getAll();
            request.setAttribute("promotionList", promotionList);

            ShippingMethodDAO shippingMethodDAO = new ShippingMethodDAO();
            List<ShippingMethod> shippingMethods = shippingMethodDAO.getAllShippingMethods();
            request.setAttribute("shippingMethods", shippingMethods);

            double tempTotal = cart.getTotalPrice();

            double discount = 0;
            Object discountObj = session.getAttribute("discount");
            if (discountObj instanceof Number) {
                discount = ((Number) discountObj).doubleValue();
            }

            double shippingFee = 0;
            String selectedShippingId = request.getParameter("shipping_method");
            if (selectedShippingId != null && !selectedShippingId.isEmpty()) {
                session.setAttribute("shipping_method", selectedShippingId);
            } else {
                selectedShippingId = (String) session.getAttribute("shipping_method");
            }

            if (selectedShippingId != null) {
                try {
                    int id = Integer.parseInt(selectedShippingId);
                    for (ShippingMethod sm : shippingMethods) {
                        if (sm.getId() == id) {
                            shippingFee = sm.getShippingFee();
                            break;
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }

            double finalTotal = tempTotal - discount + shippingFee;

            request.setAttribute("cart", cart);
            request.setAttribute("tempTotal", tempTotal);
            request.setAttribute("discount", discount);
            request.setAttribute("shippingFee", shippingFee);
            request.setAttribute("finalTotal", finalTotal);

            request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Cart cart = (Cart) session.getAttribute("cart");

            if (user == null || cart == null || cart.getList().isEmpty()) {
                response.sendRedirect("show-cart");
                return;
            }
            Boolean otpVerified = (Boolean) session.getAttribute("otp_verified");
            if (otpVerified == null || !otpVerified) {
                request.setAttribute("otpError", "Bạn cần xác minh email trước khi đặt hàng.");
                request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
                return;
            }

            String receiverName = request.getParameter("receiver_name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");

            String province = request.getParameter("province_name");
            String district = request.getParameter("district_name");
            String ward = request.getParameter("ward_name");
            String addressDetail = request.getParameter("address");
            String paymentMethod = request.getParameter("payment_method");
            String shippingMethodId = request.getParameter("shipping_method");

            String status = paymentMethod.equalsIgnoreCase("COD") ? "Chưa thanh toán" : "Đã thanh toán";

            String fullAddress = String.join(", ", ward, district, province, addressDetail);

            ShippingMethodDAO shippingMethodDAO = new ShippingMethodDAO();
            ShippingMethod selectedMethod = shippingMethodDAO.getShippingMethodById(Integer.parseInt(shippingMethodId));
            String shippingMethodName = selectedMethod != null ? selectedMethod.getMethodName() : "Không rõ";
            double shippingFee = selectedMethod != null ? selectedMethod.getShippingFee() : 0;

            double tempTotal = cart.getTotalPrice();
            double discount = session.getAttribute("discount") instanceof Number ? ((Number) session.getAttribute("discount")).doubleValue() : 0;
            double finalTotal = tempTotal - discount + shippingFee;

            InvoiceService service = new InvoiceService();
            int invoiceId = service.createInvoice(user, receiverName, phone, email, fullAddress, paymentMethod, shippingMethodName, finalTotal, shippingFee, status, cart);

            if (invoiceId > 0) {
                if (paymentMethod.equalsIgnoreCase("VNPAY")) {
                    String redirectUrl = VnpayUtils.buildPaymentUrl(invoiceId, finalTotal);
                    response.sendRedirect(redirectUrl);
                    return;
                }
                String emailBody = "<h3>Chào " + receiverName + ",</h3>" +
                        "<p>Cảm ơn bạn đã đặt hàng tại VitaminFruit.</p>" +
                        "<p>Mã hóa đơn của bạn là: <strong>#" + invoiceId + "</strong></p>" +
                        "<p>Phương thức thanh toán: " + paymentMethod + "</p>" +
                        "<p>Tổng tiền: " + finalTotal + " đ</p>" +
                        "<p>Đơn hàng của bạn sẽ được xử lý trong thời gian sớm nhất.</p>";

                vn.edu.hcmuaf.fit.project_fruit.utils.EmailUtils.sendEmail(
                        email, "Xác nhận đơn hàng #" + invoiceId, emailBody);
                session.removeAttribute("cart");
                session.removeAttribute("otp_verified");
                session.removeAttribute("order_otp");
                session.removeAttribute("order_email");
                session.removeAttribute("discount");
                request.setAttribute("orderSuccess", true);
                request.setAttribute("invoiceId", invoiceId);
                request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Thanh toán thất bại, vui lòng thử lại.");
                request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
            }
        }
    }