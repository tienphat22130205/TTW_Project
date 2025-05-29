<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="vn.edu.hcmuaf.fit.project_fruit.dao.PromotionsDao" %>
<%@ page import="vn.edu.hcmuaf.fit.project_fruit.dao.model.Promotions" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.1/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/custom-datatable.css">
    <style>
        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 999;
        }

        .invoice-modal {
            background: #ffffff;
            border-radius: 12px;
            padding: 30px;
            width: 680px;
            max-width: 95%;
            position: relative;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
            font-family: "Segoe UI", sans-serif;
            color: #333;
        }

        .modal-title {
            text-align: center;
            font-size: 24px;
            margin-bottom: 20px;
            color: #222;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }

        .invoice-info {
            display: flex;
            flex-direction: column;
            gap: 6px;
            margin-bottom: 20px;
            font-size: 15px;
        }

        .section-title {
            margin: 20px 0 10px;
            font-size: 17px;
            color: #444;
        }

        .invoice-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .invoice-table th,
        .invoice-table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: center;
            font-size: 14px;
        }

        .invoice-table th {
            background-color: #f5f5f5;
            font-weight: bold;
        }

        .total-section {
            font-size: 16px;
            margin-top: 10px;
        }

        .total-section .money {
            color: #2c7;
            font-weight: bold;
        }

        .total-section .large {
            font-size: 18px;
        }

        .badge.green {
            background-color: #d4edda;
            color: #155724;
            padding: 2px 8px;
            border-radius: 6px;
            font-size: 13px;
        }

        .close-button {
            position: absolute;
            top: 15px;
            right: 20px;
            font-size: 22px;
            cursor: pointer;
            color: #888;
            transition: color 0.2s;
        }

        .close-button:hover {
            color: red;
        }
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        #productOverlay.modal-overlay {
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            background-color: rgba(0,0,0,0.7);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            padding: 20px;
            overflow: hidden;
            height: 100vh;
        }

        #productModalContent.modal-content {
            background: #fff;
            border-radius: 12px;
            width: 1400px;
            max-width: 95%;
            padding: 30px 40px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #222;
            position: relative;
            height: 100%; /* cao full container overlay */
            display: flex;
            flex-direction: column;
        }

        #productCloseBtn.close-button {
            position: absolute;
            top: 20px;
            right: 25px;
            font-size: 28px;
            cursor: pointer;
            color: #666;
            transition: color 0.3s ease;
        }
        #productCloseBtn.close-button:hover {
            color: #e74c3c;
        }

        #productModalTitle.modal-title {
            text-align: center;
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 30px;
            border-bottom: 2px solid #eee;
            padding-bottom: 12px;
            color: #111;
        }

        .product-detail-container {
            display: flex;
            gap: 30px;
            flex: 1; /* chiếm hết chiều cao */
            overflow: hidden;
        }

        /* Ảnh sản phẩm */
        .product-image-wrapper {
            flex: 0 0 400px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .product-image-wrapper img#mainProductImage {
            width: 320px;
            height: 320px;
            object-fit: contain;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        /* Bảng thông tin */
        .product-info-wrapper {
            flex: 1;
            overflow-x: auto;
            padding-right: 10px;
        }

        .product-info-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 15px;
            color: #333;
        }

        .product-info-table th,
        .product-info-table td {
            border: 1px solid #ddd;
            padding: 12px 15px;
            vertical-align: top;
            text-align: left;
        }

        .product-info-table th {
            background-color: #f5f5f5;
            width: 160px;
            font-weight: 600;
            color: #222;
            user-select: none;
        }

        .product-info-table tr:nth-child(even) {
            background-color: #fafafa;
        }

        .product-info-table tr:hover {
            background-color: #f0f8ff;
        }

        /* Responsive cho màn hình nhỏ */
        @media (max-width: 900px) {
            #productModalContent.modal-content {
                width: 95%;
                padding: 20px;
            }
            .product-detail-container {
                flex-direction: column;
                align-items: center;
            }
            .product-image-wrapper {
                margin-bottom: 20px;
            }
            .product-info-wrapper {
                width: 100%;
            }
            .product-info-table th {
                width: 140px;
            }
        }
        .btn-circle {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            border: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: 0.3s ease;
            font-size: 16px;
            margin: 0 3px;
        }

        .btn-approve {
            background-color: #d4f5e9;
            color: #2ecc71;
        }

        .btn-approve:hover {
            background-color: #a8eecf;
        }

        .btn-cancel {
            background-color: #ffe6e6;
            color: #e74c3c;
        }

        .btn-cancel:hover {
            background-color: #f5bfbf;
        }

        .btn-icon {
            pointer-events: none;
        }
        /* Trạng thái thanh toán */
        .status-paid {
            background-color: #d4f8d4; /* xanh nhạt */
            color: #2e7d32;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }

        .status-unpaid {
            background-color: #ffe0b2; /* cam nhạt */
            color: #ef6c00;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }

        .status-canceled {
            background-color: #ffcdd2; /* đỏ nhạt */
            color: #c62828;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }

        /* Trạng thái đơn hàng */
        .order-processing {
            background-color: #fff3cd; /* vàng nhạt */
            color: #856404;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }

        .order-shipped {
            background-color: #e1f5fe; /* xanh dương nhạt */
            color: #0277bd;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }

        .order-delivered {
            background-color: #d4edda; /* xanh lá nhạt */
            color: #155724;
            padding: 4px 10px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-block;
        }
        .swal2-smaller-popup {
            font-size: 14px;
            border-radius: 8px;
        }

        .swal2-sm-btn {
            font-size: 14px !important;
            padding: 6px 16px !important;
            border-radius: 4px !important;
        }

    </style>
<body>
<input type="checkbox" name="" id="nav-toggle">
<div class="sidebar">
    <div class="sidebar-brand">
        <h1><i class="fa-solid fa-leaf"></i> <span>Fruit</span></h1>
    </div>
    <div class="sidebar-menu">
        <ul>
            <li>
                <a href="#" class="menu-item active" onclick="showSection('dashboard', 'Dashboard')"><span><i
                        class="fa-solid fa-gauge"></i></span>
                    <span>Dashboard</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('customers', 'Khách hàng')"><span><i
                        class="fa-solid fa-user"></i></span>
                    <span>Quản lý khách hàng</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('products', 'Sản phẩm')"><span><i
                        class="fa-solid fa-box"></i></span>
                    <span>Quản lý sản phẩm</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('orders', 'Đặt hàng')"><span><i
                        class="fa-solid fa-bag-shopping"></i></span>
                    <span>Quản lý đặt hàng</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('suppliers', 'Quản lý nhà cung cấp')"><span><i
                        class="fa-solid fa-truck"></i></span>
                    <span>Quản lý nhà cung cấp</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('promotions', 'Quản lý khuyến mãi')"><span><i
                        class="fa-solid fa-tags"></i></span>
                    <span>Quản lý khuyến mãi</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('feedback', 'Phản hồi khách hàng')"><span><i
                        class="fa-solid fa-comments"></i></span>
                    <span>Phản hồi khách hàng</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('statistics', 'Thống kê doanh thu')"><span><i
                        class="fa-solid fa-chart-line"></i></span>
                    <span>Thống kê doanh thu</span></a>
            </li>
            <li>
                <a href="#" class="menu-item" onclick="showSection('system', 'Hệ thống')"><span><i
                        class="fa-solid fa-user-gear"></i></span>
                    <span>Quản Lý Người Dùng</span></a>
            </li>
        </ul>
    </div>
    <div class="logout">
        <a href="#" class="menu-item" id="logoutBtn">
            <span><i class="fa-solid fa-right-from-bracket"></i></span><span>Đăng xuất</span>
        </a>
    </div>
</div>
<div class="main-content">
    <header>
        <h1 id="page-title">
            <label for="nav-toggle">
                <span class="fa-solid fa-bars"></span>
            </label>
            Dashboard
        </h1>
        <div class="user-wrapper">
            <div class="notification-icon" id="notificationBell">
                <i class="fas fa-bell"></i>
                <span class="notification-count" id="notificationCount">0</span>
            </div>
            <div class="notification-dropdown" id="notificationDropdown">
                <h3 class="notification-title">Thông báo</h3>
                <ul class="notification-list">
                    <li class="notification-item">
                        <h4>Cập nhật hệ thống</h4>
                        <p>Hệ thống sẽ được bảo trì vào lúc 12:00 AM ngày 25/11/2023.</p>
                        <span class="notification-time">20/11/2023, 10:00 AM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Cập nhật hệ thống</h4>
                        <p>Hệ thống sẽ được bảo trì vào lúc 12:00 AM ngày 25/11/2023.</p>
                        <span class="notification-time">20/11/2023, 10:00 AM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Cập nhật hệ thống</h4>
                        <p>Hệ thống sẽ được bảo trì vào lúc 12:00 AM ngày 25/11/2023.</p>
                        <span class="notification-time">20/11/2023, 10:00 AM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Cập nhật mã giảm giá mới</h4>
                        <p>Giảm giá 40% cho tất cả trái cây nhập khẩu</p>
                        <span class="notification-time">20/11/2023, 10:00 AM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Chương trình ưu đãi</h4>
                        <p>Miễn phí giao hàng cho đơn hàng trên 500.000 VNĐ.</p>
                        <span class="notification-time">18/11/2023, 8:00 PM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Khuyến mãi đặc biệt</h4>
                        <p>Mua 1 tặng 1 cho tất cả các loại táo nhập khẩu.</p>
                        <span class="notification-time">17/11/2023, 6:00 PM</span>
                    </li>
                    <li class="notification-item">
                        <h4>Khuyến mãi ngày lễ</h4>
                        <p>Giảm 20% tất cả sản phẩm nhân ngày lễ Tạ Ơn.</p>
                        <span class="notification-time">13/11/2023, 10:00 AM</span>
                    </li>
                </ul>
            </div>
            <img src="${pageContext.request.contextPath}/assets/img/anhdaidien.jpg" alt="Ảnh đại diện" width="40px" height="40px" alt="">
            <div>
                <h4>Admin</h4>
            </div>
        </div>
    </header>
    <main>
        <!-- dashboard -->
        <div id="dashboard" class="section active">
            <div class="cards">
                <div class="card-single">
                    <div>
                        <h1>245</h1>
                        <span>Khách hàng</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-users"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>500</h1>
                        <span>Sản phẩm</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-box"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>320</h1>
                        <span>Đặt hàng</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-bag-shopping"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>50</h1>
                        <span>Nhà cung cấp</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-truck"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>10</h1>
                        <span>Khuyến mãi</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-tags"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>120</h1>
                        <span>Phản hồi</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-comments"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>15%
                            <span style="font-size: 1.5rem; font-weight: normal;"> <i
                                    class="fa-solid fa-arrow-up"></i></span>
                        </h1>
                        <span>Thống kê doanh thu</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-chart-line"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>Quản Lý</h1>
                    </div>
                    <div>
                        <span class="fa-solid fa-user-gear"></span>
                    </div>
                </div>
            </div>
            <div class="recent-grid">
                <div class="customers">
                    <div class="card">
                        <div class="card-header">
                            <h2>Khách hàng gần đây</h2>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table id="recent-customers" class="display" width="100%">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên khách hàng</th>
                                        <th>Số điện thoại</th>
                                        <th>Địa chỉ</th>
                                        <th>Ngày tạo tài khoản</th>
                                        <th>Email</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <!-- Lặp qua danh sách khách hàng gần đây -->
                                    <c:forEach var="customer" items="${recentCustomers}">
                                        <tr>
                                            <td>${customer.idCustomer}</td>
                                            <td>${customer.customerName}</td>
                                            <td>${customer.customerPhone}</td>
                                            <td>${customer.address}</td>
                                            <td>${customer.dateRegister}</td>
                                            <td>${customer.email}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box">
                    <div class="flex-container">
                        <div class="product">
                            <div class="card">
                                <div class="card-header">
                                    <h2>Sản phẩm được mua nhiều nhất</h2>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table width="100%">
                                            <thead>
                                            <tr>
                                                <td>STT</td>
                                                <td>Tên sản phẩm</td>
                                                <td>Tổng số lượng mua</td>
                                                <td>Tổng số tiền</td>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <!-- Lặp qua danh sách các sản phẩm bán chạy -->
                                            <c:forEach var="product" items="${bestSellingProducts}" varStatus="status">
                                                <tr>
                                                    <td>${status.index + 1}</td>  <!-- Số thứ tự, bắt đầu từ 1 -->
                                                    <td>${product.name}</td>  <!-- Tên sản phẩm -->
                                                    <td>${product.totalQuantity}</td>  <!-- Tổng số lượng mua -->
                                                    <td>${product.totalAmount} VND</td>  <!-- Tổng số tiền -->
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dashboard">
                    <!-- Doanh thu 6 tháng qua -->
                    <div class="chart-box large-chart">
                        <h3>Doanh thu 6 tháng qua</h3>
                        <canvas id="monthlyRevenueChart"></canvas>
                    </div>

                    <!-- Doanh thu tháng vừa qua -->
                    <div class="chart-box small-chart">
                        <h3>Doanh thu tháng vừa qua</h3>
                        <canvas id="weeklyRevenueChart"></canvas>
                    </div>
                </div>

            </div>

        </div>
        <!-- Khach hang -->
        <div id="customers" class="section">
            <div class="container">
                <!-- Customer Table -->
                <table id="customerTable">
                    <thead>
                    <tr>
                        <th>Mã khách hàng</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Ngày đăng ký</th>
                        <th>Chi tiết sản phẩm đã mua</th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- Lặp qua tất cả khách hàng -->
                    <c:forEach var="customer" items="${customersUser}">
                        <tr id="customer-${customer.idCustomer}">
                            <td>${customer.idCustomer}</td>
                            <td>${customer.customerName}</td>
                            <td>${customer.email}</td>
                            <td>${customer.customerPhone}</td>
                            <td>${customer.address}</td>
                            <td>${customer.dateRegister}</td>
                            <td><button class="detail-button">Xem chi tiết</button></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Products section -->
        <div id="products" class="section">
            <div class="overview-section">
                <!-- Tổng quan sản phẩm -->
                <div class="overview-grid">
                    <div class="overview-item">
                        <h3>200</h3>
                        <p>Tổng sản phẩm</p>
                        <i class="fa-solid fa-box"></i>
                    </div>
                    <div class="overview-item">
                        <h3>80</h3>
                        <p>Sản phẩm còn hàng</p>
                        <i class="fa-solid fa-check"></i>
                    </div>
                    <div class="overview-item">
                        <h3>120</h3>
                        <p>Sản phẩm đã bán</p>
                        <i class="fa-solid fa-cart-arrow-down"></i>
                    </div>
                    <div class="overview-item">
                        <h3>4.5/5</h3>
                        <p>Đánh giá trung bình</p>
                        <i class="fa-solid fa-star"></i>
                    </div>
                </div>
            </div>
            <div class="recent-grid">
                <div class="customers">
                    <div class="card">
                        <h1>Danh sách sản phẩm</h1>
                        <div class="card-body">
                            <h3>Thêm sản phẩm</h3>
                            <form class="productAddTable" action="<%= request.getContextPath() %>/addproduct"
                                  method="post">
                                <div class="form-group">
                                    <label for="product-name">Tên sản phẩm:</label>
                                    <input type="text" id="product-name" name="product-name"
                                           placeholder="Nhập tên sản phẩm" required/>
                                </div>
                                <div class="form-group">
                                    <label for="product-type">Loại sản phẩm:</label>
                                    <input type="text" id="product-type" name="product-type"
                                           placeholder="Nhập loại sản phẩm" required/>
                                </div>
                                <div class="form-group">
                                    <label for="origin">Xuất xứ:</label>
                                    <input type="text" id="origin" name="origin" placeholder="Nhập xuất xứ" required/>
                                </div>
                                <div class="form-group">
                                    <label for="product-price">Giá:</label>
                                    <input type="text" id="product-price" name="product-price"
                                           placeholder="Nhập mức giá" required/>
                                </div>
                                <div class="form-group">
                                    <label for="quantity">Số lượng:</label>
                                    <input type="text" id="quantity" name="quantity" placeholder="Nhập số lượng"
                                           required/>
                                </div>
                                <div class="form-group">
                                    <label for="promotion-code">Mã khuyến mãi:</label>
                                    <input type="text" id="promotion-code" name="promotion-code"
                                           placeholder="Nhập mã khuyến mãi" required/>
                                </div>
                                <div class="form-group">
                                    <label for="supplier-add">Mã nhà cung cấp:</label>
                                    <input type="text" id="supplier-add" name="supplier-add"
                                           placeholder="Nhập mã nhà cung cấp" required/>
                                </div>
                                <div class="form-group">
                                    <label for="warranty-period">Thời gian bảo hành:</label>
                                    <input type="text" id="warranty-period" name="warranty-period"
                                           placeholder="Nhập thời gian bảo hành" required/>
                                </div>
                                <div class="form-group">
                                    <label for="shelf-life">Hạn sử dụng:</label>
                                    <input type="text" id="shelf-life" name="shelf-life" placeholder="Nhập hạn sử dụng"
                                           required/>
                                </div>
                                <div class="form-group">
                                    <label for="describe">Mô tả:</label>
                                    <input type="text" id="describe" name="describe" placeholder="Nhập mô tả sản phẩm"
                                           required/>
                                </div>
                                <div class="form-group">
                                    <label for="rating">Đánh giá:</label>
                                    <input type="text" id="rating" name="rating" placeholder="Nhập đánh giá sản phẩm"
                                           required/>
                                </div>
                                <div class="form-group">
                                    <label for="characteristic">Đặc điểm:</label>
                                    <input type="text" id="characteristic" name="characteristic"
                                           placeholder="Nhập đặc điểm sản phẩm" required/>
                                </div>
                                <div class="form-group">
                                    <label for="preserve-product">Bảo quản:</label>
                                    <input type="text" id="preserve-product" name="preserve-product"
                                           placeholder="Nhập cách bảo quản" required/>
                                </div>
                                <div class="form-group">
                                    <label for="use-product">Hướng dẫn sử dụng:</label>
                                    <input type="text" id="use-product" name="use-product"
                                           placeholder="Nhập hướng dẫn sử dụng" required/>
                                </div>
                                <div class="form-group">
                                    <label for="benefit">Lợi ích:</label>
                                    <input type="text" id="benefit" name="benefit" placeholder="Nhập lợi ích sản phẩm"
                                           required/>
                                </div>
                                <div class="form-group">
                                    <label for="image-link">Link ảnh:</label>
                                    <input type="text" id="image-link" name="image-link" placeholder="Nhập link ảnh"
                                           required/>
                                </div>
                                <button type="submit" class="btn-submit">Cập nhật</button>
                            </form>

                            <div class="table-reponsive">
                                <table id="productTable" class="product-table">
                                    <thead>
                                    <tr>
                                        <th>Mã Sản Phẩm</th>
                                        <th>Tên Sản Phẩm</th>
                                        <th>Loại Sản phẩm</th>
                                        <th>Xuất xứ</th>
                                        <th>Giá Sản Phẩm</th>
                                        <th>Trạng thái</th>
                                        <th>Chi tiết</th>
                                        <th>Thao tác</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <!-- Lặp qua danh sách sản phẩm -->
                                    <c:forEach var="product" items="${products}">
                                        <tr>
                                            <td>${product.id_product}</td> <!-- Mã sản phẩm -->
                                            <td>${product.name}</td> <!-- Tên sản phẩm -->
                                            <td>${product.categoryName}</td> <!-- Loại sản phẩm -->
                                            <td>${product.origin}</td> <!-- Xuất xứ sản phẩm -->
                                            <td>${product.price}</td> <!-- Xuất xứ sản phẩm -->
                                            <td>
                                                <span class="status ${product.status ? 'blue' : 'red'}"></span>
                                                    ${product.status ? 'Còn Hàng' : 'Hết Hàng'}
                                            </td>
                                            <td>
                                                <button onclick='openProductOverlayFromButton(this)'
                                                        data-product='${fn:escapeXml(productJsonMap[product.id_product])}'>
                                                    Xem chi tiết
                                                </button>
                                            </td>

                                            <td>
                                                <button class="edit-button" onclick="window.location.href='edit-product?pid=${product.id_product}'">Chỉnh sửa</button>
                                                <button class="delete-button" onclick="window.location.href='remove-product?pid=${product.id_product}'">Xóa</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Orders section -->
        <div id="orders" class="section">
            <div class="orders">
                <div class="overview-grid">
                    <div class="overview-item">
                        <h3>${totalOrders}</h3>
                        <p>Tổng đơn hàng</p>
                        <i class="fa-solid fa-boxes-stacked"></i>
                    </div>
                    <div class="overview-item">
                        <h3>${processingOrders}</h3>
                        <p>Đơn hàng đang xử lý</p>
                        <i class="fa-solid fa-hourglass-start"></i>
                    </div>
                    <div class="overview-item">
                        <h3>${paidOrders}</h3>
                        <p>Đơn hàng đã thanh toán</p>
                        <i class="fa-regular fa-handshake"></i>
                    </div>
                    <div class="overview-item">
                        <h3>${cancelledOrders}</h3>
                        <p>Đơn hàng đã hủy</p>
                        <i class="fa-regular fa-circle-xmark"></i>
                    </div>
                </div>
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="orderTable" class="display" width="100%">
                                <thead>
                                <tr>
                                    <th>Họ tên</th>
                                    <th>SĐT</th>
                                    <th>Chi tiết hóa đơn</th>
                                    <th>Phương thức thanh toán</th>
                                    <th>Tình trạng thanh toán</th>
                                    <th>Tình trạng đơn hàng</th>
                                    <th>Hành động</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="invoice" items="${invoices}">
                                    <tr>
                                        <td>${invoice.accountName}</td>
                                        <td>${invoice.phone}</td>
                                        <td>
                                            <button onclick='openInvoiceDetail({
                                                    id: "${invoice.idInvoice}",
                                                    name: "${invoice.receiverName}",
                                                    phone: "${invoice.phone}",
                                                    email: "${invoice.email}",
                                                    address: "${invoice.addressFull}",
                                                    paymentMethod: "${invoice.paymentMethod}",
                                                    status: "${invoice.status}",
                                                    createdAt: "${invoice.createDate}",
                                                    accountName: "${invoice.accountName}",
                                                    shippingFee: ${invoice.shippingFee},
                                                    totalPrice: ${invoice.totalPrice != null ? invoice.totalPrice.intValue() : 0}
                                                    })'>
                                                Xem chi tiết
                                            </button>
                                        </td>
                                        <td>${invoice.paymentMethod}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${invoice.status == 'Đã thanh toán'}">
                                                    <span class="badge status-paid">Đã thanh toán</span>
                                                </c:when>
                                                <c:when test="${invoice.status == 'Đã hủy'}">
                                                    <span class="badge status-canceled">Đã hủy</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge status-unpaid">Chưa thanh toán</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <!-- Tình trạng đơn hàng -->
                                        <td class="order-status">
                                            <c:choose>
                                                <c:when test="${invoice.orderStatus == 'Đang xử lý'}">
                                                    <span class="badge order-processing">Đang xử lý</span>
                                                </c:when>
                                                <c:when test="${invoice.orderStatus == 'Đã giao'}">
                                                    <span class="badge order-shipped">Đã giao</span>
                                                </c:when>
                                                <c:when test="${invoice.orderStatus == 'Đã hủy'}">
                                                    <span class="badge order-canceled">Đã hủy</span>
                                                </c:when>
                                                <c:when test="${invoice.orderStatus == 'Đang chuẩn bị đơn hàng'}">
                                                    <span class="badge order-processing">Đang chuẩn bị đơn hàng</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge order-delivered">${invoice.orderStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${invoice.status == 'Chưa thanh toán'}">
                                                <div id="action-${invoice.idInvoice}" data-id="${invoice.idInvoice}" class="action-buttons">
                                                    <button class="btn-circle btn-approve" onclick="handleAction(${invoice.idInvoice}, 'approve')">
                                                        <i class="fas fa-check btn-icon"></i>
                                                    </button>
                                                    <button class="btn-circle btn-cancel" onclick="handleAction(${invoice.idInvoice}, 'cancel')">
                                                        <i class="fas fa-times btn-icon"></i>
                                                    </button>
                                                </div>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>

            </div>

        </div>
        <!-- Statistics section -->
        <div id="statistics" class="section">
            <div class="chart-box large-chart">
                <h3>Doanh thu theo tháng</h3>
                <canvas id="monthlyRevenueChart1"></canvas>
            </div>
            <div class="chart-container">
                <div class="chart-box large-chart">
                    <h3>Các sản phẩm đóng góp doanh thu nhiều nhất</h3>
                    <canvas id="productDonutChart"></canvas>
                </div>
                <div class="chart-box common-chart">
                    <h3>Doanh thu theo Loại sản phẩm tháng vừa qua</h3>
                    <canvas id="productTypeRevenueChart"></canvas>
                </div>
            </div>
        </div>
        <div id="suppliers" class="section">
            <div class="container">
                <!-- Supplier Table -->
                <table id="supplierTable">
                    <thead>
                    <tr>
                        <th>Mã số</th>
                        <th>Tên nhà cung cấp</th>
                        <th>Địa chỉ</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Trạng thái hợp tác</th>
                        <th>Đánh giá</th>
                        <th>Danh sách sản phẩm</th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- Lặp qua tất cả nhà cung cấp -->
                    <c:forEach var="supplier" items="${suppliers}">
                        <tr id="supplier-${supplier.id_supplier}">
                            <td>${supplier.id_supplier}</td>
                            <td>${supplier.name}</td>
                            <td>${supplier.address}</td>
                            <td>${supplier.email}</td>
                            <td>${supplier.phone_number}</td>
                            <td>
                                    ${supplier.status}
                                <c:choose>
                                    <c:when test="${supplier.status == 'Đang hợp tác'}">
                                        <i class="fas fa-circle" style="color: blue;"></i>
                                    </c:when>
                                    <c:when test="${supplier.status == 'Đã dừng'}">
                                        <i class="fas fa-circle" style="color: red;"></i>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${supplier.rating} <i class="fas fa-star" style="color: #ffcc00;"></i></td>
                            <td>${supplier.name_category}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="promotions" class="section">
            <div class="promotion-container">
                <div class="promotion-header">
                    <h1>Quản Lý Khuyến Mãi</h1>
                </div>
                <!-- Form Thêm Khuyến Mãi -->
                <h3>Thêm khuyến mãi</h3>
                <form class="promotionAddTable" action="<%= request.getContextPath() %>/AddPromotionServlet"
                      method="POST">
                    <div class="form-group">
                        <label for="promotion-code">Tên khuyến mãi:</label>
                        <input type="text" id="promotion-code" name="promotion_code" placeholder="Nhập mã giảm giá"
                               required/>
                    </div>

                    <div class="form-group">
                        <label for="description-add">Mô tả:</label>
                        <input type="text" id="description-add" name="description_add" placeholder="Nhập mô tả"
                               required/>
                    </div>

                    <div class="form-group">
                        <label for="start-date">Ngày bắt đầu:</label>
                        <input type="date" id="start-date" name="start_date" required/>
                    </div>

                    <div class="form-group">
                        <label for="expiration-date">Ngày hết hạn:</label>
                        <input type="date" id="expiration-date" name="expiration_date" required/>
                    </div>

                    <div class="form-group">
                        <label for="promotion-discount">Mức giảm (%):</label>
                        <input type="number" id="promotion-discount" name="promotion_discount"
                               placeholder="Nhập mức giảm (%)" min="0" max="100" required/>
                    </div>

                    <div class="form-group">
                        <label for="promotion-type">Loại:</label>
                        <select id="promotion-type" name="promotion_type" class="promotionType" required>
                            <option value="weekly">Weekly</option>
                            <option value="general">General</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-submit">Cập nhật</button>
                </form>

                <h3>Danh sách Khuyến mãi</h3>
                <div class="promotion-table">
                    <%
                        PromotionsDao promotionsDao = new PromotionsDao();
                        List<Promotions> promotionsList = promotionsDao.getAll();
                    %>
                    <table id="promotionTable">
                        <thead>
                        <tr style="text-align: center">
                            <th>ID</th>
                            <th style="text-align: left">Tên Khuyến Mãi</th>
                            <th>Mô Tả</th>
                            <th>Ngày Bắt Đầu</th>
                            <th>Ngày Kết Thúc</th>
                            <th>Phần Trăm Giảm Giá</th>
                            <th>Loại</th>
                            <th>Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Promotions promotion : promotionsList) {
                        %>
                        <tr>
                            <td><%= promotion.getId_promotion() %>
                            </td>
                            <td><%= promotion.getPromotion_name() %>
                            </td>
                            <td><%= promotion.getDescribe_1() %>
                            </td>
                            <td><%= promotion.getStart_date() %>
                            </td>
                            <td style="text-align: center"><%= promotion.getEnd_date() %>
                            </td>
                            <td style="text-align: center"><%= promotion.getPercent_discount()%>%
                            </td>
                            <td style="text-align: center"><%= promotion.getType() %>
                            </td>
                            <td>
                                <button onclick="openModal({promoTitle: '', promoDiscount: 0, promoStart: '', promoEnd: ''}, 'editPromotion')">Sửa</button>
                                <button onclick="window.location.href='remove-promotion?pid=<%= promotion.getId_promotion() %>'">Xóa</button>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="feedback" class="section">
            <div class="feedback-container">
                <div class="feedback-content">
                    <h1>Phản Hồi Khách Hàng</h1>
                    <table id="feedbackTable" class="feedback-table">
                        <thead>
                        <tr>
                            <th>ID Feedback</th>
                            <th>Tên sản phẩm</th>
                            <th>Tên khách hàng</th>
                            <th>Nội dung</th>
                            <th>Ngày tạo</th>
                            <th>Đánh giá</th>
                        </tr>
                        </thead>
                        <tbody>
                        <!-- Lặp qua danh sách feedback -->
                        <c:forEach var="feedback" items="${feedback}">
                            <tr>
                                <td>${feedback.idFeedback}</td>
                                <td>${feedback.productName}</td>
                                <td>${feedback.cusName}</td>
                                <td>${feedback.content}</td>
                                <td>${feedback.dateCreate}</td>
                                <td style="gap: 5px">${feedback.rating} <i class="fas fa-star"></i></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="system" class="section">
            <div class="system-settings">
                <div class="system-menu">
                    <!-- Tab Quản lý tài khoản -->
                    <div class="tab-content">
                        <h2>QUẢN LÝ TÀI KHOẢN</h2>
                        <!-- Khu vực thêm tài khoản -->
                        <div class="account-management">
                            <!-- Form thêm tài khoản mới -->
                            <div class="account-form">
                                <h3>Thêm Tài Khoản</h3>
                                <form class="accountAddTable" action="<%= request.getContextPath() %>/AddAccountServlet"
                                      method="post">
                                    <!-- Họ và tên -->
                                    <div class="form-group">
                                        <label for="username">Họ và tên:</label>
                                        <input type="text" id="username" name="username" placeholder="Nhập họ và tên"
                                               required/>
                                    </div>
                                    <!-- Email -->
                                    <div class="form-group">
                                        <label for="email">Email:</label>
                                        <input type="email" id="email" name="email" placeholder="Nhập email" required/>
                                    </div>
                                    <!-- Mật khẩu -->
                                    <div class="form-group">
                                        <label for="password">Mật khẩu:</label>
                                        <input type="password" id="password" name="password" placeholder="Nhập mật khẩu"
                                               minlength="6" required/>
                                    </div>
                                    <!-- Xác nhận mật khẩu -->
                                    <div class="form-group">
                                        <label for="confirm-password">Xác nhận mật khẩu:</label>
                                        <input type="password" id="confirm-password" name="confirm-password"
                                               placeholder="Xác nhận mật khẩu" minlength="6" required/>
                                    </div>
                                    <!-- Vai trò -->
                                    <div class="form-group">
                                        <label for="role">Phân quyền:</label>
                                        <select id="role" name="role" class="accountRole" required>
                                            <option value="" disabled selected>Chọn vai trò</option>
                                            <option value="admin">Quản trị viên</option>
                                            <option value="staff">Nhân viên</option>
                                        </select>
                                    </div>
                                    <!-- Nút thêm tài khoản -->
                                    <div class="form-group">
                                        <button type="submit" class="btn-submit">Thêm tài khoản</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Danh sách tài khoản -->
                        <div id="account-list">
                            <h3>DANH SÁCH TÀI KHOẢN</h3>
                            <table id = "userAdmin">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên đăng nhập</th>
                                    <th>Email</th>
                                    <th>Phân quyền</th>
                                    <th>Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="customer" items="${AdminStaff}">
                                    <tr id="customer-${customer.idCustomer}">
                                        <td>${customer.idCustomer}</td>
                                        <td>${customer.customerName}</td>
                                        <td>${customer.email}</td>
                                        <td>${customer.role}</td>
                                        <td>
                                            <button onclick="window.location.href='remove-account?pid=${customer.idCustomer}'">
                                                Xóa
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
<div id="logoutOverlay" class="logout-overlay" style="display: none;"></div>
<div id="logoutNotification" class="logout-notification" style="display: none;">
    <div class="notification-content">
        <p>Bạn có muốn đăng xuất ?</p>
        <button id="confirmLogoutBtn">Có</button>
        <button id="cancelLogoutBtn">Không</button>
    </div>
</div>

<div id="deleteOverlay" class="delete-overlay" style="display: none;"></div>
<div id="deleteNotification" class="delete-notification" style="display: none;">
    <div class="notification-content">
        <p>Bạn có muốn xóa sản phẩm này?</p>
        <button id="confirmDeleteBtn">Có</button>
        <button id="cancelDeleteBtn">Không</button>
    </div>
</div>
<%--chi tiết hóa đơn--%>
<div id="invoiceOverlay" class="modal-overlay">
    <div class="modal-content invoice-modal">
        <span class="close-button" onclick="document.getElementById('invoiceOverlay').style.display='none'">&times;</span>
        <h2 class="modal-title">🧾 Chi tiết đơn hàng</h2>

        <div class="invoice-info">
            <div><strong>Mã đơn hàng:</strong> <span id="invoiceIdDisplay"></span></div>
            <div><strong>Tên người nhận:</strong> <span id="customerName"></span></div>
            <div><strong>Ngày tạo:</strong> <span id="createdAt"></span></div>
            <div><strong>Địa chỉ nhận hàng:</strong> <span id="address"></span></div>
            <div><strong>Phí vận chuyển:</strong> <span id="shippingFee" class="badge green"></span></div>
        </div>

        <h4 class="section-title">🛒 Danh sách sản phẩm</h4>
        <table class="invoice-table">
            <thead>
            <tr>
                <th>#</th>
                <th>Sản phẩm</th>
                <th>Số lượng</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody id="invoiceProductBody">
            </tbody>
        </table>

        <div class="total-section">
            <p><strong>Tổng thanh toán:</strong> <span id="totalPrice" class="money large"></span></p>
        </div>
    </div>
</div>
<!-- Overlay chi tiết sản phẩm -->
<div id="productOverlay" class="modal-overlay" style="display:none;">
    <div id="productModalContent" class="modal-content">
        <span id="productCloseBtn" class="close-button" onclick="closeProductOverlay()">&times;</span>
        <h2 id="productModalTitle" class="modal-title">📦 Chi tiết sản phẩm</h2>

        <div class="product-detail-container">
            <!-- Ảnh lớn bên trái -->
            <div class="product-image-wrapper">
                <div id="productImagesDisplay" class="product-images"></div>
            </div>

            <!-- Thông tin chi tiết bên phải dùng bảng -->
            <div class="product-info-wrapper">
                <table class="product-info-table">
                    <tbody>
                    <tr>
                        <th>Mã sản phẩm</th>
                        <td id="productIdDisplay"></td>
                    </tr>
                    <tr>
                        <th>Tên sản phẩm</th>
                        <td id="productNameDisplay"></td>
                    </tr>
                    <tr>
                        <th>Xuất xứ</th>
                        <td id="productOriginDisplay"></td>
                    </tr>
                    <tr>
                        <th>Giá</th>
                        <td><span id="productPriceDisplay"></span> đ</td>
                    </tr>
                    <tr>
                        <th>Đánh giá</th>
                        <td id="productRatingDisplay"></td>
                    </tr>
                    <tr>
                        <th>Trạng thái</th>
                        <td id="productStatusDisplay"></td>
                    </tr>
                    <tr>
                        <th>Mô tả</th>
                        <td id="productDescribeDisplay"></td>
                    </tr>
                    <tr>
                        <th>Số lượng</th>
                        <td id="productQuantityDisplay"></td>
                    </tr>
                    <tr>
                        <th>Ngày nhập</th>
                        <td id="productEntryDateDisplay"></td>
                    </tr>
                    <tr>
                        <th>Hạn sử dụng</th>
                        <td id="productShelfLifeDisplay"></td>
                    </tr>
                    <tr>
                        <th>Thời gian bảo hành</th>
                        <td id="productWarrantyDisplay"></td>
                    </tr>
                    <tr>
                        <th>Đặc điểm</th>
                        <td id="productCharacteristicDisplay"></td>
                    </tr>
                    <tr>
                        <th>Cách bảo quản</th>
                        <td id="productPreserveDisplay"></td>
                    </tr>
                    <tr>
                        <th>Cách sử dụng</th>
                        <td id="productUseDisplay"></td>
                    </tr>
                    <tr>
                        <th>Lợi ích</th>
                        <td id="productBenefitDisplay"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.13.1/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js"></script>
<script>
    $(document).ready(function () {
        // Khởi tạo DataTable cho tất cả các bảng
        $('#feedbackTable, #supplierTable, #customerTable, #productTable, #promotionTable, #orderTable, #userAdmin').DataTable({
            paging: true, // Kích hoạt phân trang
            searching: true, // Kích hoạt tìm kiếm
            ordering: true, // Kích hoạt sắp xếp
            pageLength: 10, // Số dòng hiển thị mỗi trang
            lengthMenu: [5, 10, 20, 50], // Các tùy chọn số dòng mỗi trang
            language: {
                search: "Tìm kiếm:",
                lengthMenu: "Hiển thị _MENU_ dòng mỗi trang",
                info: "Hiển thị từ _START_ đến _END_ của _TOTAL_ dòng",
                paginate: {
                    previous: "Trước",
                    next: "Tiếp"
                }
            }
        });
    });
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js" defer></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const logoutBtn = document.getElementById("logoutBtn");
        const overlay = document.getElementById("logoutOverlay");
        const popup = document.getElementById("logoutNotification");
        const confirmBtn = document.getElementById("confirmLogoutBtn");
        const cancelBtn = document.getElementById("cancelLogoutBtn");

        // Mở overlay xác nhận khi nhấn nút đăng xuất
        logoutBtn.addEventListener("click", function (e) {
            e.preventDefault(); // Ngăn reload trang
            overlay.style.display = "block";
            popup.style.display = "block";
        });

        // Nhấn "Không" để hủy
        cancelBtn.addEventListener("click", function () {
            overlay.style.display = "none";
            popup.style.display = "none";
        });

        // Nhấn "Có" để đăng xuất → gọi Servlet /logout
        confirmBtn.addEventListener("click", function () {
            window.location.href = "logout";
        });

        // Nhấn ra ngoài cũng tắt
        overlay.addEventListener("click", function () {
            overlay.style.display = "none";
            popup.style.display = "none";
        });
    });
</script>
<script>
    function openInvoiceDetail(invoice) {
        document.getElementById('invoiceOverlay').style.display = 'flex';

        document.getElementById('invoiceIdDisplay').innerText = invoice.id;
        document.getElementById('customerName').innerText = invoice.name;
        document.getElementById('createdAt').innerText = invoice.createdAt;
        document.getElementById('address').innerText = invoice.address;
        document.getElementById('shippingFee').innerText = invoice.shippingFee.toLocaleString('vi-VN') + ' đ';
        document.getElementById('totalPrice').innerText = invoice.totalPrice.toLocaleString('vi-VN') + ' đ';

        const body = document.getElementById("invoiceProductBody");
        body.innerHTML = "";

        // ✅ Sửa tại đây
        const contextPath = "/" + window.location.pathname.split("/")[1];
        const fullUrl = `${contextPath}/admin/invoice-detail?id=${invoice.id}`;
        console.log("📤 Fetch URL:", fullUrl);

        fetch(fullUrl)
            .then(res => {
                if (!res.ok) throw new Error("Lỗi khi gọi API chi tiết hóa đơn");
                return res.json();
            })
            .then(products => {
                console.log("📦 Sản phẩm nhận được:", products);

                if (!products || products.length === 0) {
                    body.innerHTML = `<tr><td colspan="5" style="color:red;">Không có sản phẩm nào.</td></tr>`;
                    return;
                }

                products.forEach((p, index) => {
                    console.log(`🧾 [${index}]`, p);
                    const subtotal = p.quantity * p.price * (1 - p.discount / 100);
                    const row = `
            <tr>
                <td>${index + 1}</td>
                <td>${p.name}</td>
                <td>${p.quantity}</td>
                <td>${p.price.toLocaleString("vi-VN")} đ</td>
                <td>${subtotal.toLocaleString("vi-VN")} đ</td>
            </tr>`;
                    console.log("📋 Dòng HTML tạo ra:", row);
                    body.innerHTML += row;
                });
            })
            .catch(err => {
                console.error("❌ Lỗi khi fetch chi tiết sản phẩm:", err);
                body.innerHTML = `<tr><td colspan="5" style="color:red;">Không thể tải danh sách sản phẩm.</td></tr>`;
            });
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    function handleAction(id, action) {
        const actionText = action === 'approve' ? 'duyệt đơn hàng' : 'hủy đơn hàng';
        const actionLabel = action === 'approve' ? 'Duyệt' : 'Hủy';

        Swal.fire({
            text: `Bạn có chắc chắn muốn ${actionText} #${id}?`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#28a745',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Có',
            cancelButtonText: 'Không',
            width: 320,
            padding: '1em',
            backdrop: true,
            customClass: {
                popup: 'swal2-smaller-popup',
                confirmButton: 'swal2-sm-btn',
                cancelButton: 'swal2-sm-btn'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                fetch('${pageContext.request.contextPath}/admin/approve-order', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: 'id=' + id + '&action=' + action
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'success') {
                            const actionWrapper = document.querySelector('[data-id="' + id + '"]');
                            if (actionWrapper) {
                                actionWrapper.innerHTML = '';

                                const statusCell = actionWrapper.closest('tr').querySelector('td:nth-child(5)');
                                if (statusCell) {
                                    statusCell.innerHTML = (action === 'approve')
                                        ? '<span class="badge status-paid">Đã thanh toán</span>'
                                        : '<span class="badge status-canceled">Đã hủy</span>';
                                }

                                const orderStatusCell = actionWrapper.closest('tr').querySelector('.order-status');
                                if (orderStatusCell) {
                                    orderStatusCell.innerHTML = (action === 'approve')
                                        ? '<span class="badge order-processing">Đang chuẩn bị đơn hàng</span>'
                                        : '<span class="badge order-canceled">Đã hủy</span>';
                                }
                            }

                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: `${actionLabel} đơn hàng #${id} thành công`,
                                timer: 1500,
                                showConfirmButton: false,
                                width: 320
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: data.message,
                                width: 320
                            });
                        }
                    });
            }
        });
    }
</script>
<script>
    function openProductOverlayFromButton(button) {
        const jsonStr = button.getAttribute('data-product');
        try {
            const product = JSON.parse(jsonStr);
            openProductOverlay(product);
        } catch (e) {
            console.error("Lỗi parse JSON:", e);
        }
    }
    function openProductOverlay(product) {
        document.getElementById('productIdDisplay').textContent = product.id_product || 'N/A';
        document.getElementById('productNameDisplay').textContent = product.name || 'N/A';
        document.getElementById('productOriginDisplay').textContent = product.origin || '';
        document.getElementById('productPriceDisplay').textContent = product.price !== undefined ? product.price : 'N/A';
        document.getElementById('productRatingDisplay').textContent = product.rating || '';
        document.getElementById('productStatusDisplay').textContent = product.status ? 'Còn hàng' : 'Hết hàng';
        document.getElementById('productDescribeDisplay').textContent = product.describe_1 || '';
        document.getElementById('productQuantityDisplay').textContent = product.quantity !== undefined ? product.quantity : '';
        document.getElementById('productEntryDateDisplay').textContent = product.entry_date || '';
        document.getElementById('productShelfLifeDisplay').textContent = product.shelf_life || '';
        document.getElementById('productWarrantyDisplay').textContent = product.warranty_period || '';
        document.getElementById('productCharacteristicDisplay').textContent = product.characteristic || '';
        document.getElementById('productPreserveDisplay').textContent = product.preserve_product || '';
        document.getElementById('productUseDisplay').textContent = product.use_prodcut || '';
        document.getElementById('productBenefitDisplay').textContent = product.benefit || '';

        const imagesDiv = document.getElementById('productImagesDisplay');
        imagesDiv.innerHTML = ''; // Xóa ảnh cũ nếu có

        if (product.listImg && Array.isArray(product.listImg) && product.listImg.length > 0) {
            product.listImg.forEach(img => {
                const imgEl = document.createElement('img');
                imgEl.src = img.url;  // url ảnh
                imgEl.alt = product.name || 'Product Image';
                imgEl.style.width = '320px';
                imgEl.style.height = 'auto';
                imgEl.style.borderRadius = '6px';
                imagesDiv.appendChild(imgEl);
            });
        } else {
            imagesDiv.textContent = 'Không có ảnh sản phẩm.';
        }
        document.getElementById('productOverlay').style.display = 'flex';
    }

    function closeProductOverlay() {
        document.getElementById('productOverlay').style.display = 'none';
    }
</script>

</body>

</html>