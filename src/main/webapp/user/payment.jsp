<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán đơn giản</title>
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
    </script>
</head>
<body>
<div class="container">
    <div class="content">
        <!-- Bên trái: Thông tin đơn hàng -->
        <div class="left">
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
            <div class="cart-item">
                <img src="https://via.placeholder.com/50" alt="Sản phẩm 1">
                <span>Sản phẩm 1 - 500,000đ</span>
            </div>
            <div class="cart-item">
                <img src="https://via.placeholder.com/50" alt="Sản phẩm 2">
                <span>Sản phẩm 2 - 250,000đ</span>
            </div>
            <div class="voucher">
                <label for="voucher">Mã giảm giá:</label>
                <input type="text" id="voucher" placeholder="Nhập mã giảm giá nếu có">
            </div>
            <h3>Tổng tiền: <strong>750,000đ</strong></h3>
        </div>
    </div>
</div>
</body>
</html>