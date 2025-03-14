<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment.css">
</head>

<body>
<div class="checkout-container">
    <div class="header">
        <button class="back-button" onclick="goBack()">
            <i class="fas fa-arrow-left"></i> Quay lại
        </button>
        <h2 class="page-title">Thanh toán</h2>
    </div>
    <div class="tab-navigation">
        <div class="tab active" id="infoTabButton" onclick="switchTab('infoTab')">1. THÔNG TIN</div>
        <div class="tab" id="paymentTabButton">2. THANH TOÁN</div>
    </div>
    <div id="paymentTab" class="tab-content">
        <div id="discountForm" class="discount-form">
            <div class="discount-form">
                <div class="discount-input-container">
                    <input type="text" id="discountCode" placeholder="Nhập mã giảm giá (chỉ áp dụng 1 lần)">
                    <button onclick="applyDiscount()">Áp dụng</button>
                </div>
                <p class="available-discounts" onclick="toggleAvailableDiscounts()">hoặc chọn từ 1 mã giảm giá có
                    sẵn</p>
                <div id="discountList" class="discount-list">
                    <div class="discount-item" onclick="selectDiscount('S-Student')">Giảm S-Student - 400.000đ</div>
                    <div class="discount-item" onclick="selectDiscount('Freeship')">Giảm giá Freeship - Miễn phí vận
                        chuyển</div>
                    <div class="discount-item" onclick="selectDiscount('Discount10')">Giảm 10% - Tối đa 200.000đ
                    </div>
                </div>
            </div>
            <div class="summary">
                <p>Số lượng sản phẩm: <span id="productQuantity">01</span></p>
                <p>Tiền hàng (tạm tính): <span id="subtotal">20.590.000đ</span></p>
                <p>Phí vận chuyển: <span id="shippingFee">Miễn phí</span></p>
                <p>Giảm giá: <span id="discountAmount">- 400.000đ</span></p>
                <p><small>Quyền lợi dành riêng cho Học sinh - Sinh viên</small></p>
                <h3>Tổng tiền (đã gồm VAT): <span id="totalAmount">20.590.000đ</span></h3>
            </div>
        </div>
        <div class="section-header">Thông tin thanh toán</div>
        <div class="payment-option" onclick="openOverlay()">
            <div class="option-icon" id="selectedIcon">
                <img src="../assets/img/logoBank/hinhthucthanhtoan.png" alt="Default Icon">
            </div>
            <div class="option-text">
                <p id="selectedPaymentMethod" class="main-text">Chọn phương thức thanh toán</p>
                <p class="sub-text">Nhận thêm nhiều ưu đãi tại cổng</p>
            </div>
        </div>
        <div class="delivery-info">
            <h3>THÔNG TIN NHẬN HÀNG</h3>
            <div class="delivery-details">
                <p>Khách hàng: <span id="customerName"></span></p>
                <p>Số điện thoại: <span id="customerPhone"></span></p>
                <p>Email: <span id="customerEmail"></span></p>
                <p>Nhận hàng tại: <span id="deliveryAddress"></span></p>
                <p>Người nhận: <span id="receiverInfo"></span></p>
            </div>
        </div>
        <div class="checkout-summary">
            <div class="total-summary">
                <p class="summary-text">Tổng tiền tạm tính:</p>
                <p class="summary-amount">20.590.000đ</p>
            </div>
            <button>Thanh toán</button>
        </div>
    </div>
    <div class="information">
        <div id="infoTab" class="tab-content active">
            <!-- Sản phẩm -->
            <div class="section">
                <div class="section-header">SẢN PHẨM</div>
                <div class="section-content">
                    <!-- Kiểm tra nếu giỏ hàng không rỗng -->
                    <c:if test="${not empty cart.list}">
                        <c:forEach var="item" items="${cart.list}">
                            <div class="product-summary">
                                <!-- Hiển thị hình ảnh sản phẩm -->
                                <c:if test="${not empty item.listImg and not empty item.listImg[0].url}">
                                    <img src="${item.listImg[0].url}" alt="${item.name}">
                                </c:if>
                                <!-- Hiển thị thông tin sản phẩm -->
                                <div>
                                    <h4>${item.name}</h4>
                                    <p>Giá: ${item.price}₫</p>
                                    <p>Số lượng: ${item.quantity}</p>
                                    <p><strong>Tổng: ${item.price * item.quantity}₫</strong></p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty cart.list}">
                        <p>Giỏ hàng trống!</p>
                    </c:if>
                </div>
            </div>
            <!-- Thông tin khách hàng -->
            <div class="section">
                <div class="section-header">THÔNG TIN KHÁCH HÀNG</div>
                <div class="section-content">
                    <div class="row">
                        <div class="col">
                            <label for="name">Họ và tên:</label>
                            <input type="text" id="name" name="name" value="${user.name}" placeholder="Nhập họ và tên">
                        </div>
                        <div class="col">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${user.email}" placeholder="Nhập email">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <label for="phone">Số điện thoại:</label>
                            <input type="text" id="phone" name="phone" value="${user.phone}" placeholder="Nhập số điện thoại">
                        </div>
                    </div>
                </div>
            </div>
            <c:out value="${cart}" />
            <!-- Thông tin nhận hàng -->
            <div class="section">
                <div class="section-header">THÔNG TIN NHẬN HÀNG</div>
                <div class="section-content">
                    <div class="delivery-method">
                        <div class="method-option active" onclick="selectMethod(this, 'storePickupContent')">Nhận
                            tại cửa hàng</div>
                        <div class="method-option" onclick="selectMethod(this, 'homeDeliveryContent')">Giao hàng tận
                            nơi</div>
                    </div>
                    <!-- Nội dung của từng hình thức -->
                    <div id="storePickupContent" class="method-content active-content">
                        <label for="storeBranch">Chọn chi nhánh:</label>
                        <select id="storeBranch">
                            <option>Chi nhánh 1 - 43 Nguyễn Thái Học</option>
                            <option>Chi nhánh 2 - 28 Mai Chí Thọ</option>
                        </select>
                    </div>

                    <div id="homeDeliveryContent" class="method-content">
                        <div class="row">
                            <div class="col">
                                <label for="province">Tỉnh/Thành phố:</label>
                                <select id="province">
                                    <option>Hồ Chí Minh</option>
                                </select>
                            </div>
                            <div class="col">
                                <label for="district">Quận/Huyện:</label>
                                <select id="district">
                                    <option>Quận 1</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">
                                <label for="ward">Phường/Xã:</label>
                                <select id="ward">
                                    <option>Phường 1</option>
                                </select>
                            </div>
                            <div class="col">
                                <label for="address">Số nhà, tên đường:</label>
                                <input type="text" id="address" placeholder="Nhập địa chỉ">
                            </div>
                        </div>
                        <label for="note">Ghi chú khác (nếu có):</label>
                        <textarea id="note" placeholder="Nhập ghi chú..."></textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="tip-and-invoice">
            <div class="invoice-request">
                <label class="invoice-checkbox">
                    <input type="checkbox" id="companyInvoice" />
                    <span>Yêu cầu xuất hóa đơn công ty</span>
                </label>
                <p class="invoice-note">
                    Với đơn hàng <strong>trên 20 triệu đồng</strong>, vui lòng thanh toán bằng phương thức chuyển
                    khoản từ tài khoản công ty hoặc bằng thẻ công ty.
                    <a href="#" class="policy-link">Xem chính sách</a>
                </p>
            </div>
        </div>
        <div class="checkout-summary">
            <div class="total-summary">
                <p class="summary-text">Tổng tiền tạm tính:</p>
                <p class="summary-amount">${cart.totalPrice}₫</p>
            </div>
            <button class="continue-button" onclick="nextTab()">Tiếp tục</button>
        </div>
    </div>
    <div id="overlay" class="overlay hidden">
        <div class="overlay-content">
            <div class="overlay-header">
                <h3>Chọn phương thức thanh toán</h3>
                <button class="close-button" onclick="closeOverlay()">×</button>
            </div>
            <div class="overlay-body">
                <ul class="payment-methods">
                    <li onclick="selectPaymentMethod('Thanh toán khi nhận hàng', '../assets/img/logoBank/thanhtoankhinhanhang.png')">
                        <img src="../assets/img/logoBank/thanhtoankhinhanhang.png" alt="Cash Icon"> Thanh toán khi nhận hàng
                    </li>
                    <li onclick="selectPaymentMethod('Chuyển khoản ngân hàng qua mã QR', '../assets/img/logoBank/thanhtoanqr.png')">
                        <img src="../assets/img/logoBank/thanhtoanqr.png" alt="QR Icon"> Chuyển khoản ngân hàng qua mã QR
                    </li>
                    <li onclick="selectPaymentMethod('VNPAY', '../assets/img/logoBank/vnpay.jpg')">
                        <img src="../assets/img/logoBank/vnpay.jpg" alt="VNPAY Icon"> VNPAY
                    </li>
                    <li onclick="selectPaymentMethod('Qua thẻ Visa/Master/JCB/Napas', '../assets/img/logoBank/visa.png')">
                        <img src="../assets/img/logoBank/visa.png" alt="Card Icon"> Qua thẻ Visa/Master/JCB/Napas
                    </li>
                    <li onclick="selectPaymentMethod('Ví MoMo', '../assets/img/logoBank/momo.webp')">
                        <img src="../assets/img/logoBank/momo.webp" alt="MoMo Icon"> Ví MoMo
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <script src="../assets/js/payment.js"></script>
</body>

</html>
