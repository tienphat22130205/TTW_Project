<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán đơn giản</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 1200px;
            margin: auto;
            padding: 20px;
            background: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            margin-top: 50px;
        }
        .content {
            display: flex;
            flex-wrap: wrap;
        }
        .left, .right {
            flex: 1;
            min-width: 300px;
            padding: 20px;
            box-sizing: border-box;
        }
        .right {
            border-left: 2px solid #ddd;
        }
        label {
            font-weight: bold;
        }
        select, input {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        .btn-container {
            display: flex;
            justify-content: space-between;
            gap: 10px;
        }
        button {
            flex: 1;
            padding: 10px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .cod {
            background: green;
            color: white;
        }
        .transfer {
            background: blue;
            color: white;
        }
        .cart-item {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
        }
        .cart-item img {
            width: 50px;
            height: 50px;
            margin-right: 10px;
            border-radius: 5px;
        }
        .submit-btn {
            width: 100%;
            padding: 12px;
            background: #ff6600;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 15px;
        }
        .voucher {
            margin-top: 20px;
        }
        @media (max-width: 768px) {
            .content {
                flex-direction: column;
            }
            .right {
                border-left: none;
                border-top: 2px solid #ddd;
            }
        }
        .loader {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #009688;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            animation: spin 0.8s linear infinite;
            display: inline-block;
            margin-left: 10px;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

    </style>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            fetch("https://provinces.open-api.vn/api/?depth=3")
                .then(response => response.json())
                .then(data => {
                    const provinceSelect = document.getElementById("province");
                    const districtSelect = document.getElementById("district");
                    const wardSelect = document.getElementById("ward");

                    data.forEach(province => {
                        let option = document.createElement("option");
                        option.value = province.code;
                        option.textContent = province.name;
                        provinceSelect.appendChild(option);
                    });

                    provinceSelect.addEventListener("change", function () {
                        const selectedProvince = data.find(p => p.code == this.value);
                        districtSelect.innerHTML = "<option value=''>Chọn Quận/Huyện</option>";
                        wardSelect.innerHTML = "<option value=''>Chọn Phường/Xã</option>";

                        selectedProvince.districts.forEach(district => {
                            let option = document.createElement("option");
                            option.value = district.code;
                            option.textContent = district.name;
                            districtSelect.appendChild(option);
                        });
                    });

                    districtSelect.addEventListener("change", function () {
                        const selectedProvince = data.find(p => p.code == provinceSelect.value);
                        const selectedDistrict = selectedProvince.districts.find(d => d.code == this.value);
                        wardSelect.innerHTML = "<option value=''>Chọn Phường/Xã</option>";

                        selectedDistrict.wards.forEach(ward => {
                            let option = document.createElement("option");
                            option.value = ward.code;
                            option.textContent = ward.name;
                            wardSelect.appendChild(option);
                        });
                    });
                });
        });
        // load xử lý
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.querySelector(".voucher form");
            const button = form.querySelector("button");

            form.addEventListener("submit", function (e) {
                e.preventDefault(); // Ngăn submit ngay lập tức
                button.disabled = true;
                button.innerHTML = 'Đang xử lý <span class="loader"></span>';

                // Giả lập delay 2.5 giây rồi mới submit
                setTimeout(() => {
                    form.submit();
                }, 2500);
            });
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="container">
    <div class="content">
        <!-- Bên trái: Thông tin đơn hàng -->
        <div class="left">
            <a href="${pageContext.request.contextPath}/home" style="
        display: inline-flex;
        align-items: center;
        gap: 8px;
        background: #eee;
        color: #333;
        text-decoration: none;
        padding: 8px 12px;
        border-radius: 6px;
        font-weight: bold;
        margin-bottom: 10px;
    ">
                <i class="fas fa-arrow-left"></i> Tiếp tục mua hàng
            </a>
            <h2>Thông tin thanh toán</h2>
            <form>
                <label>Họ và Tên:</label>
                <input type="text" required>

                <label>Số điện thoại:</label>
                <input type="tel" required>

                <label>Email:</label>
                <input type="email" required>

                <label>Tỉnh/Thành phố:</label>
                <select id="province">
                    <option value="">Chọn Tỉnh/Thành phố</option>
                </select>

                <label>Quận/Huyện:</label>
                <select id="district">
                    <option value="">Chọn Quận/Huyện</option>
                </select>

                <label>Phường/Xã:</label>
                <select id="ward">
                    <option value="">Chọn Phường/Xã</option>
                </select>

                <label>Số nhà, địa chỉ cụ thể:</label>
                <input type="text" required>

                <h3>Phương thức thanh toán</h3>
                <div class="btn-container">
                    <button type="button" class="cod">Nhận hàng rồi trả tiền (COD)</button>
                    <button type="button" class="transfer">Chuyển khoản</button>
                </div>

                <button type="submit" class="submit-btn">Xác nhận thanh toán</button>
            </form>
        </div>
        <!-- Bên phải: Hiển thị sản phẩm và tổng tiền -->
        <div class="right">
            <h2>Giỏ hàng</h2>
                <c:forEach var="item" items="${cart.getList()}">
                    <div class="cart-item">
                        <c:if test="${not empty item.listImg}">
                            <img src="${item.listImg[0].url}" alt="${item.name}" width="50" />
                        </c:if>
                        <c:if test="${empty item.listImg}">
                            <img src="${pageContext.request.contextPath}/assets/img/default.png" alt="No image" width="50" />
                        </c:if>
                        <span>
            ${item.name} -
            <fmt:formatNumber value="${item.price * item.quantity}" type="currency" currencySymbol="₫" />
        </span>
                    </div>
                </c:forEach>
            <div class="voucher">
                <form action="apply-voucher" method="post" style="display: flex; gap: 10px; flex-wrap: wrap;">
                    <label for="voucher" style="width: 100%;">Mã giảm giá:</label>
                    <input type="text" name="voucherCode" id="voucher" placeholder="Nhập mã giảm giá" style="flex: 2;">
                    <button type="submit" class="submit-btn" style="flex: 1; background: #009688;">Áp dụng</button>
                </form>

                <c:choose>
                    <c:when test="${not empty discountSuccess}">
                        <p style="color: green;">${discountSuccess}</p>
                    </c:when>
                    <c:when test="${not empty discountError}">
                        <p style="color: red;">${discountError}</p>
                    </c:when>
                </c:choose>
            </div>

            <c:choose>
                <c:when test="${not empty newTotalPrice}">
                    <h3>Tổng tiền sau giảm:
                        <strong style="color: red;">
                            <fmt:formatNumber value="${newTotalPrice}" type="currency" currencySymbol="₫"/>
                        </strong>
                    </h3>
                    <p>
                        (Đã giảm:
                        <fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/>
                        với mã: <strong>${appliedPromotion.code}</strong>)
                    </p>
                </c:when>
                <c:otherwise>
                    <h3>Tổng tiền:
                        <strong>
                            <fmt:formatNumber value="${cart.getTotalPrice()}" type="currency" currencySymbol="₫"/>
                        </strong>
                    </h3>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<c:if test="${not empty discountSuccess}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Thành công!',
            text: '${discountSuccess}',
            showConfirmButton: false,
            timer: 2500
        });
    </script>
    <%
        session.removeAttribute("discountSuccess");
    %>
</c:if>

<c:if test="${not empty discountError}">
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: '${discountError}',
            showConfirmButton: true
        });
    </script>
    <%
        session.removeAttribute("discountError");
    %>
</c:if>
</body>
</html>