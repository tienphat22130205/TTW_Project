<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/custom-datatable.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <style>
        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
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
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.7);
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
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
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

        #editProductOverlay.modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.7);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            padding: 10px 20px; /* giảm padding tổng */
            overflow: hidden;
            height: 100vh;
        }

        #editProductOverlay .modal-content {
            background: #fff;
            border-radius: 12px;
            width: 1100px; /* giảm chiều rộng */
            max-width: 95%;
            max-height: 85vh; /* giới hạn chiều cao */
            padding: 20px 30px; /* giảm padding */
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #222;
            position: relative;
            display: flex;
            flex-direction: column;
        }

        /* Phần chứa ảnh + form chính */
        #editProductOverlay .product-detail-container {
            display: flex;
            gap: 20px;
            flex: 1;
            overflow: hidden;
            max-height: calc(85vh - 100px); /* trừ header + footer */
        }

        /* Ảnh sản phẩm bên trái */
        #editProductOverlay .product-image-wrapper {
            flex: 0 0 300px; /* giảm kích thước ảnh */
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding-top: 10px;
        }

        #editProductOverlay .product-image-wrapper img {
            width: 280px; /* nhỏ hơn để cân đối */
            height: auto;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        /* Bảng form bên phải */
        #editProductOverlay .product-info-wrapper {
            flex: 1;
            overflow-y: auto; /* cuộn dọc */
            min-width: 0; /* để tránh tràn */
        }

        /* Bảng form */
        #editProductOverlay .product-info-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px; /* nhỏ hơn 1 chút */
            color: #333;
        }

        #editProductOverlay .product-info-table th,
        #editProductOverlay .product-info-table td {
            border: 1px solid #ddd;
            padding: 8px 12px; /* giảm padding */
            vertical-align: top;
            text-align: left;
        }

        #editProductOverlay .product-info-table th {
            background-color: #f5f5f5;
            width: 150px;
            font-weight: 600;
            color: #222;
            user-select: none;
        }

        #editProductOverlay .product-info-table tr:nth-child(even) {
            background-color: #fafafa;
        }

        #editProductOverlay .product-info-table tr:hover {
            background-color: #f0f8ff;
        }

        /* Nút bấm dưới */
        #editProductOverlay form > div:last-child {
            margin-top: 1em;
            text-align: center;
            gap: 12px;
            display: flex;
            justify-content: center;
        }

        #editProductOverlay button {
            padding: 8px 18px;
            border: none;
            border-radius: 6px;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        #editEnableBtn {
            background-color: #3498db;
            color: white;
        }

        #editEnableBtn:hover:not(:disabled) {
            background-color: #2980b9;
        }

        #editSaveBtn {
            background-color: #27ae60;
            color: white;
        }

        #editSaveBtn:hover:not(:disabled) {
            background-color: #1e8449;
        }

        #editProductOverlay button[disabled] {
            opacity: 0.6;
            cursor: not-allowed;
        }

        #editProductOverlay button:nth-child(3) {
            background-color: #e74c3c;
            color: white;
        }

        #editProductOverlay button:nth-child(3):hover {
            background-color: #c0392b;
        }

        /* Responsive cho màn hình nhỏ */
        @media (max-width: 900px) {
            #editProductOverlay .modal-content {
                width: 95%;
                padding: 15px 20px;
                max-height: 90vh;
            }

            #editProductOverlay .product-detail-container {
                flex-direction: column;
                max-height: none;
                overflow: visible;
            }

            #editProductOverlay .product-image-wrapper {
                margin-bottom: 20px;
                flex: none;
            }

            #editProductOverlay .product-info-wrapper {
                width: 100%;
                overflow: visible;
            }

            #editProductOverlay .product-info-table th {
                width: 140px;
            }
        }

        #editProductOverlay form > div:last-child {
            margin-top: 1.5em;
            text-align: center;
            display: flex;
            justify-content: center;
            gap: 15px; /* khoảng cách giữa các nút */
            padding-bottom: 10px; /* cách đáy modal */
            border-top: 1px solid #eee; /* gạch nhẹ phía trên */
        }

        /* chung cho cả 3 nút */
        #editProductOverlay button {
            min-width: 110px; /* chiều rộng tối thiểu */
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            box-shadow: 0 3px 6px rgb(0 0 0 / 0.1);
        }

        /* Nút Chỉnh sửa - màu xanh dương */
        #editEnableBtn {
            background-color: #3498db;
            color: white;
        }

        #editEnableBtn:hover:not(:disabled) {
            background-color: #2980b9;
        }

        /* Nút Lưu - màu xanh lá */
        #editSaveBtn {
            background-color: #27ae60;
            color: white;
        }

        #editSaveBtn:hover:not(:disabled) {
            background-color: #1e8449;
        }

        /* Nút bị disabled sẽ mờ */
        #editProductOverlay button[disabled] {
            opacity: 0.6;
            cursor: not-allowed;
        }

        /* Nút Đóng - màu đỏ */
        #editProductOverlay button:nth-child(3) {
            background-color: #e74c3c;
            color: white;
        }

        #editProductOverlay button:nth-child(3):hover {
            background-color: #c0392b;
        }

        #custom-toast-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
        }

        .custom-toast {
            display: flex;
            align-items: center;
            background-color: #ffffff;
            border-left: 5px solid #28a745;
            border-radius: 6px;
            padding: 12px 16px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 10px;
            min-width: 280px;
            animation: slideIn 0.4s ease;
            position: relative;
            font-family: Arial, sans-serif;
            color: #000 !important;
        }

        .custom-toast.success .toast-icon {
            color: #28a745;
        }

        .toast-icon {
            font-size: 18px;
            margin-right: 10px;
        }

        .toast-message {
            flex: 1;
            font-size: 14px;
            color: #000000;
        }

        .toast-close {
            background: none;
            border: none;
            font-size: 16px;
            cursor: pointer;
            color: #aaa;
            position: absolute;
            top: 8px;
            right: 10px;
        }

        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
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

        .dashboard-summary-box {
            padding: 24px;
            border-radius: 12px;
            background-color: #fff;
            box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        .summary-title {
            font-size: 20px;
            margin-bottom: 24px;
            font-weight: 600;
        }

        .summary-grid {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        .summary-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px 20px;
            border-radius: 8px;
            background: #f9f9f9;
            box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.03);
        }

        .summary-item .icon {
            font-size: 22px;
            margin-right: 12px;
        }

        .summary-item .label {
            flex: 1;
            text-align: left;
            font-weight: 500;
            color: #444;
        }

        .summary-item .value {
            font-weight: 700;
            color: #111;
        }

        .icon.green {
            color: #2ecc71;
        }

        .icon.blue {
            color: #3498db;
        }

        .icon.red {
            color: #e74c3c;
        }

        .contact-button {
            background-color: #6294f6; /* màu vàng nhẹ */
            color: #d9e3f4;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
        }

        .contact-button:hover {
            background-color: #417dfa; /* màu vàng nhẹ */
            color: #d9e3f4;
        }
        .user-profile {
            position: relative;
            display: inline-block;
            cursor: pointer;
        }

        .user-tooltip {
            position: absolute;
            top: 110%;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(40, 40, 40, 0.95);
            color: white;
            padding: 8px 12px;
            border-radius: 6px;
            white-space: nowrap;
            font-weight: 600;
            font-family: Arial, sans-serif;
            font-size: 14px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.3);
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.3s ease;
            z-index: 1000;
        }

        /* Mũi tên dưới tooltip */
        .user-tooltip::after {
            content: "";
            position: absolute;
            bottom: 100%; /* dưới tooltip */
            left: 50%;
            transform: translateX(-50%);
            border-width: 6px;
            border-style: solid;
            border-color: rgba(40, 40, 40, 0.95) transparent transparent transparent;
        }

        .user-profile:hover .user-tooltip {
            opacity: 1;
            pointer-events: auto;
        }
        #topCustomersTable thead th tr td{
            text-align: center;
            vertical-align: middle; /* nếu muốn dọc cũng căn giữa */
        }
    </style>
    <script>
        function showCustomToast(message, type = 'success') {
            const container = document.getElementById("custom-toast-container");

            const toast = document.createElement("div");
            toast.className = `custom-toast ${type}`;

            toast.innerHTML = `
                <div class="toast-icon">✅</div>
                <div class="toast-message">${message}</div>
                <button class="toast-close" onclick="this.parentElement.remove()">×</button>
              `;
            container.appendChild(toast);

            setTimeout(() => {
                toast.remove();
            }, 3000);
        }
    </script>
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
                <a href="#" class="menu-item" onclick="showSection('system', 'Quản lý người dùng')"><span><i
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


            <!-- chuong thong bao -->
            <div class="notification-icon" id="notificationBell_Menu">
                <a href="#" id="notificationToggle" class="notification-link" style="color: #000000">
                    <i class="fas fa-bell"></i>
                    <span class="notification-label">Thông báo</span>
                    <span class="notification-count" id="notificationCount">0</span>
                </a>
            </div>
            <div class="notification-popup" id="notificationPopup">
                <div class="notification-popup-header">Thông báo mới</div>
                <table class="notification-table">
                    <thead>
                    </thead>
                    <tbody>
                    <!-- Các dòng thông báo sẽ được chèn vào đây bằng JS -->
                    </tbody>
                </table>
            </div>

            <style>
                .notification-icon {
                    display: flex;
                    position: relative;
                    font-size: 26px;
                    user-select: none;
                    cursor: pointer;
                }

                .notification-link {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    text-decoration: none;
                    gap: 4px;
                    color: #222;
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    padding-right: 30px;
                    transition: color 0.2s ease;
                }

                .notification-link:hover {
                    color: #007bff;
                }

                .notification-label {
                    font-size: 13px;
                    font-weight: 600;
                    white-space: nowrap;
                    user-select: none;
                }

                .notification-count {
                    position: absolute;
                    top: -6px;
                    right: 20px;
                    background-color: #e03e2f; /* đỏ đậm */
                    color: #fff;
                    border-radius: 50%;
                    min-width: 20px;
                    height: 20px;
                    line-height: 20px;
                    padding: 0 6px;
                    font-size: 12px;
                    font-weight: 700;
                    text-align: center;
                    box-shadow: 0 0 4px rgba(224, 62, 47, 0.6);
                    user-select: none;
                }

                .notification-link:hover i.fas.fa-bell {
                    animation: bell-shake 0.6s ease-in-out;
                    color: #e03e2f;
                }

                @keyframes bell-shake {
                    0%, 100% {
                        transform: rotate(0deg);
                    }
                    25% {
                        transform: rotate(-15deg);
                    }
                    50% {
                        transform: rotate(15deg);
                    }
                    75% {
                        transform: rotate(-10deg);
                    }
                }

                .notification-popup {
                    position: absolute;
                    right: 40px; /* tăng khoảng cách tránh avatar bên phải */
                    top: 58px;
                    margin-top: 8px;
                    width: 360px;
                    max-height: 420px;
                    overflow-y: auto;
                    background-color: #ffffff;
                    color: #333;
                    border-radius: 12px;
                    box-shadow: 0 8px 24px rgba(0,0,0,0.15);
                    z-index: 9999;

                    padding: 8px 12px;
                    box-sizing: border-box;

                    opacity: 0;
                    pointer-events: none;
                    transform: translateY(-10px);
                    transition: opacity 0.25s ease, transform 0.25s ease;
                }

                .notification-popup.active {
                    opacity: 1;
                    pointer-events: auto;
                    transform: translateY(0);
                }

                .notification-popup-header {
                    position: sticky;
                    top: 0;
                    background-color: #fafafa;
                    padding: 14px 20px;
                    font-weight: 700;
                    font-size: 18px;
                    border-bottom: 1px solid #ddd;
                    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
                    border-radius: 12px 12px 0 0;
                    user-select: none;
                }

                /* Bảng thông báo */
                .notification-table {
                    width: 100%;
                    border-collapse: collapse;
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    font-size: 14px;
                    color: #444;
                    table-layout: fixed;
                    word-wrap: break-word;
                }

                .notification-table thead tr {
                    background-color: #f1f4ff;
                    font-weight: 600;
                    text-align: left;
                    border-bottom: 2px solid #a0b4ff;
                }

                .notification-table th,
                .notification-table td {
                    padding: 12px 10px;
                    border-bottom: 1px solid #eee;
                    vertical-align: middle;
                    overflow-wrap: break-word;
                }

                .notification-table tbody tr:hover {
                    background-color: #f0f4ff;
                    cursor: default;
                }

                .notification-table tbody tr:last-child td {
                    border-bottom: none;
                }

                /* Thông báo khi không có thông báo */
                .no-notification {
                    padding: 12px;
                    color: #666;
                    font-style: italic;
                    text-align: center;
                }

            </style>
            <script>
                const bell = document.getElementById("notificationToggle");
                const popup = document.getElementById("notificationPopup");
                const countSpan = document.getElementById("notificationCount");
                const notificationTableBody = popup.querySelector('tbody');

                let oldNotificationCount = 0;

                // Hàm tải thông báo và cập nhật bảng nếu có thay đổi số lượng
                async function pollNotifications() {
                    try {
                        const response = await fetch('/project_fruit/admin/notifications');
                        if (!response.ok) throw new Error('Lỗi tải thông báo');

                        const notifications = await response.json();

                        if (!Array.isArray(notifications)) {
                            console.warn('Dữ liệu thông báo không phải mảng');
                            return; // không cập nhật UI nếu dữ liệu không đúng
                        }

                        if (notifications.length !== oldNotificationCount) {
                            oldNotificationCount = notifications.length;

                            notificationTableBody.innerHTML = '';

                            if (notifications.length === 0) {
                                const tr = document.createElement('tr');
                                const td = document.createElement('td');
                                td.colSpan = 1;  // 1 cột nội dung
                                td.classList.add('no-notification');
                                td.textContent = 'Không có thông báo mới';
                                tr.appendChild(td);
                                notificationTableBody.appendChild(tr);

                                countSpan.style.display = "none";
                                countSpan.textContent = "";
                            } else {
                                notifications.forEach(item => {
                                    const tr = document.createElement('tr');
                                    const tdContent = document.createElement('td');

                                    if (item.afterData) {
                                        tdContent.textContent = item.afterData;
                                    } else if (item.content) {
                                        tdContent.textContent = item.content;
                                    } else if (typeof item === 'string') {
                                        tdContent.textContent = item;
                                    } else {
                                        tdContent.textContent = JSON.stringify(item);
                                    }

                                    tr.appendChild(tdContent);
                                    notificationTableBody.appendChild(tr);
                                });

                                countSpan.style.display = "inline-block";
                                countSpan.textContent = notifications.length;
                            }
                        }
                    } catch (error) {
                        console.error('Lỗi khi lấy thông báo:', error);
                    }
                }

                // Gọi polling định kỳ 5 giây
                setInterval(pollNotifications, 5000);

                // Gọi lần đầu khi load trang
                document.addEventListener('DOMContentLoaded', () => {
                    pollNotifications();
                });

                // Bật/tắt popup khi click icon chuông, gọi API đánh dấu đã xem khi bật
                bell.addEventListener("click", async (event) => {
                    event.preventDefault();
                    popup.classList.toggle("active");

                    if (popup.classList.contains("active")) {
                        try {
                            const res = await fetch('/project_fruit/admin/notifications/mark-seen', { method: 'POST' });
                            if (res.ok) {
                                oldNotificationCount = 0;
                                countSpan.style.display = "none";
                                countSpan.textContent = "";
                                pollNotifications();
                            } else {
                                console.error('Lỗi đánh dấu đã xem, status:', res.status);
                            }
                        } catch (error) {
                            console.error('Lỗi đánh dấu đã xem:', error);
                        }
                    }
                });

                // Ẩn popup khi click ra ngoài popup và icon chuông
                document.addEventListener("click", (event) => {
                    if (!bell.contains(event.target) && !popup.contains(event.target)) {
                        popup.classList.remove("active");
                    }
                });

            </script>
            <!-- chuong thong bao -->

            <div class="user-profile">
                <img src="${pageContext.request.contextPath}/assets/img/anhdaidien.jpg" alt="Ảnh đại diện" width="40px" height="40px" />
                <div class="user-tooltip">
                    <h4><span class="role">${fn:toUpperCase(sessionScope.role)}</span> : ${sessionScope.fullname}</h4>
                </div>
            </div>
            <div>
                <h4>VitaminFruit</h4>
            </div>
        </div>
    </header>
    <main>
        <!-- dashboard -->
        <div id="dashboard" class="section active">
            <div class="cards">
                <div class="card-single">
                    <div>
                        <h1>${totalCustomers1}</h1>
                        <span>Khách hàng</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-users"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>${totalProductsAdmin}</h1>
                        <span>Sản phẩm</span>
                    </div>
                    <div>
                        <span class="fa-solid fa-box"></span>
                    </div>
                </div>
                <div class="card-single">
                    <div>
                        <h1>${totalOrders}</h1>
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
                        <h1>
                            <c:choose>
                                <c:when test="${growthPercent != null}">
                                    <fmt:formatNumber value="${growthPercent}" maxFractionDigits="1"/>%
                                    <i class="fa-solid fa-arrow-up" style="color: green;"></i>
                                </c:when>
                                <c:otherwise>
                                    Không có dữ liệu
                                </c:otherwise>
                            </c:choose>
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
                                    <h2>Top khách hàng chi tiêu cao nhất</h2>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table id="topCustomersTable" width="100%">
                                            <thead>
                                            <tr>
                                                <td>STT</td>
                                                <td>Họ tên</td>
                                                <td>SĐT</td>
                                                <td>Địa chỉ</td>
                                                <td>Tổng chi tiêu</td>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="customer" items="${topCustomers}" varStatus="loop">
                                                <tr>
                                                    <td>${loop.index + 1}</td>
                                                    <td>${customer.fullname}</td>
                                                    <td>${customer.phone}</td>
                                                    <td>${customer.address}</td>
                                                    <td><fmt:formatNumber value="${customer.totalSpent}" type="currency"
                                                                          currencySymbol="₫"/></td>
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
                        <h3>Đơn hàng theo trạng thái trong tháng</h3>
                        <canvas id="orderStatusChart"></canvas>
                    </div>

                    <!-- Tổng kết tháng này -->
                    <div class="chart-box small-chart dashboard-summary-box">
                        <h3 class="summary-title">Tổng kết tháng này</h3>
                        <div class="summary-grid">
                            <div class="summary-item">
                                <div class="icon green">💵</div>
                                <div class="label">Doanh thu :</div>
                                <div class="value" id="summary-revenue">0 đ</div>
                            </div>
                            <div class="summary-item">
                                <div class="icon blue">🧾</div>
                                <div class="label">Tổng đơn hàng :</div>
                                <div class="value" id="summary-orders">0</div>
                            </div>
                            <div class="summary-item">
                                <div class="icon red">❌</div>
                                <div class="label">Đơn bị hủy :</div>
                                <div class="value" id="summary-canceled">0</div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>
        <!-- Khach hang -->
        <c:if test="${role == 'admin'}">
        <div id="customers" class="section">
            <div class="container">
                <!-- Customer Table -->
                <table id="customerTable">
                    <thead>
                    <tr>
                        <th>Mã khách hàng</th>
                        <th>Tên khách hàng</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Chi tiết khách hàng</th>
                        <th>Ngày đăng ký</th>
                        <th>Hành động</th>
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
                            <td>
                                <button class="detail-button" onclick="window.location.href='customer-detail?id=${customer.idCustomer}'">Xem chi tiết</button>
                            </td>
                            <td>${customer.dateRegister}</td>
                            <td>
                                <button class="edit-button" data-customer='${fn:escapeXml(customerJsonMap[customer.idCustomer])}'>Chỉnh sửa</button>
                                <button class="delete-button" onclick="window.location.href='remove-customer?id=${customer.idCustomer}'">Xóa</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        </c:if>
        <c:if test="${role == 'admin'}">
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
                            <h3>Danh sách sản phẩm</h3>
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
                                                <!-- Thay vì onclick inline, dùng data attribute chứa JSON -->
                                                <button class="edit-button" data-product='${fn:escapeXml(productJsonMap[product.id_product])}'>Chỉnh sửa</button>
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
        </c:if>
        <div id="orders" class="section">
            <c:choose>
            <c:when test="${role == 'admin' || role == 'staff'}">
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
                <div class="card" style="margin-bottom: 20px;">
                    <div class="card-header">
                        <h3>Đơn hàng mới trong 24 giờ</h3>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <c:choose>
                            <c:when test="${not empty newInvoices}">
                            <table id="newOrderTable" class="display" width="100%">
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
                                <c:forEach var="invoice" items="${newInvoices}">
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
                            </c:when>
                            <c:otherwise>
                                <p style="padding: 10px; font-weight: bold;">Hôm nay không có đơn hàng nào.</p>
                            </c:otherwise>
                            </c:choose>
                        </div>
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
                                            <c:if test="${role == 'admin' || role == 'staff'}">
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
            </c:when>
                <c:otherwise>
                    <p>Bạn không có quyền truy cập phần này.</p>
                </c:otherwise>
            </c:choose>

        </div>
        <c:if test="${role == 'admin'}">
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
                    <h3>Doanh thu theo phương thức thanh toán</h3>
                    <canvas id="paymentMethodRevenueChart"></canvas>
                </div>
            </div>
        </div>
        </c:if>
        <c:if test="${role == 'admin'}">
        <div id="suppliers" class="section">
            <div class="card">
                <div class="card-body">
                    <h3>Thêm nhà cung cấp</h3>
                    <form class="supplierAddForm" action="<%= request.getContextPath() %>/addsupplier" method="post">
                        <div class="form-group">
                            <label for="supplier-name">Tên nhà cung cấp:</label>
                            <input type="text" id="supplier-name" name="supplier-name"
                                   placeholder="Nhập tên nhà cung cấp" required/>
                        </div>

                        <div class="form-group">
                            <label for="supplier-email">Email:</label>
                            <input type="email" id="supplier-email" name="supplier-email" placeholder="Nhập email"
                                   required/>
                        </div>

                        <div class="form-group">
                            <label for="supplier-phone">Số điện thoại:</label>
                            <input type="text" id="supplier-phone" name="supplier-phone"
                                   placeholder="Nhập số điện thoại" required/>
                        </div>

                        <div class="form-group">
                            <label for="supplier-address">Địa chỉ:</label>
                            <input type="text" id="supplier-address" name="supplier-address" placeholder="Nhập địa chỉ"
                                   required/>
                        </div>

                        <div class="form-group">
                            <label for="supplier-rating">Đánh giá:</label>
                            <input type="text" id="supplier-rating" name="supplier-rating"
                                   placeholder="Nhập đánh giá (số sao)" required/>
                        </div>

                        <div class="form-group">
                            <label for="fruit-type">Loại trái cây cung cấp:</label>
                            <select id="fruit-type" name="fruit-type" required>
                                <option value="">-- Chọn loại trái cây --</option>
                                <option value="Cam">Cam</option>
                                <option value="Táo">Táo</option>
                                <option value="Xoài">Xoài</option>
                                <option value="Chuối">Chuối</option>
                                <option value="Dưa hấu">Dưa hấu</option>
                                <!-- Bạn có thể thay danh sách này bằng dữ liệu từ DB -->
                            </select>
                        </div>
                        <button type="submit" class="btn-submit">Thêm nhà cung cấp</button>
                    </form>
                    <h3>Danh sách nhà cung cấp</h3>
                    <div class="table-responsive">
                        <table id="supplierTable" class="display" width="100%">
                            <thead>
                            <tr>
                                <th>Tên nhà cung cấp</th>
                                <th>Email</th>
                                <th>Số điện thoại</th>
                                <th>Trạng thái hợp tác</th>
                                <th>Đánh giá</th>
                                <th>Hành động</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="supplier" items="${suppliers}">
                                <tr id="supplier-${supplier.id_supplier}">
                                    <td>${supplier.name}</td>
                                    <td>${supplier.email}</td>
                                    <td>${supplier.phone_number}</td>
                                    <td>
                                            ${supplier.status}
                                        <c:choose>
                                            <c:when test="${supplier.status == 'Đang hợp tác'}">
                                                <i class="fas fa-circle" style="color: #d4f8d4;"></i>
                                            </c:when>
                                            <c:when test="${supplier.status == 'Đã dừng'}">
                                                <i class="fas fa-circle" style="color: #ffcdd2;"></i>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                            ${supplier.rating} <i class="fas fa-star" style="color: #ffeb98;"></i>
                                    </td>
                                    <td>
                                        <!-- Nút chỉnh sửa: truyền thông tin nhà cung cấp bằng data attribute -->
                                        <button class="edit-button"
                                                data-supplier='${fn:escapeXml(supplierJsonMap[supplier.id_supplier])}'>
                                            Chỉnh sửa
                                        </button>

                                        <!-- Nút xóa: chuyển hướng bằng URL -->
                                        <button class="delete-button"
                                                onclick="window.location.href='remove-supplier?id=${supplier.id_supplier}'">
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
        </c:if>
        <c:if test="${role == 'admin'}">
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
                        <label for="promotion-name">Tên khuyến mãi:</label>
                        <input type="text" id="promotion-name" name="promotion_name" placeholder="Nhập tên khuyến mãi"
                               required/>
                    </div>

                    <div class="form-group">
                        <label for="promotion-code">Mã khuyến mãi:</label>
                        <input type="text" id="promotion-code" name="promotion_code"
                               placeholder="Nhập mã giảm giá" required/>
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
                        <label for="min-order-amount">Giá trị đơn tối thiểu (VNĐ):</label>
                        <input type="number" id="min-order-amount" name="min_order_amount"
                               placeholder="Nhập giá trị tối thiểu" min="0" required/>
                    </div>

                    <div class="form-group">
                        <label for="max-usage">Số lượt sử dụng tối đa:</label>
                        <input type="number" id="max-usage" name="max_usage" placeholder="Nhập số lượt tối đa" min="1"
                               required/>
                    </div>

                    <button type="submit" class="btn-submit">Thêm khuyến mãi</button>
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
                            <td>
                                <%= promotion.getPromotion_name() %>
                            </td>
                            <td>
                                <%= promotion.getDescribe_1() %>
                            </td>
                            <td>
                                <%= promotion.getStart_date() %>
                            </td>
                            <td>
                                <%= promotion.getEnd_date() %>
                            </td>
                            <td>
                                <%= promotion.getPercent_discount()%>%
                            </td>
                            <td>
                                <%= promotion.getType() %>
                            </td>
                            <td>
                                <button class="edit-button" onclick="openModal({promoTitle: '', promoDiscount: 0, promoStart: '', promoEnd: ''}, 'editPromotion')">Chỉnh sửa</button>
                                <button class="delete-button" onclick="window.location.href='remove-promotion?pid=<%= promotion.getId_promotion() %>'">Xóa</button>
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
        </c:if>
        <c:if test="${role == 'admin'}">
        <div id="feedback" class="section">
            <div class="feedback-container">
                <div class="feedback-content">
                    <h1>Phản Hồi Khách Hàng</h1>
                    <table id="feedbackTable" class="feedback-table">
                        <thead>
                        <tr>
                            <th>Tên sản phẩm</th>
                            <th>Tên khách hàng</th>
                            <th>Nội dung</th>
                            <th>Ngày tạo</th>
                            <th>Đánh giá</th>
                            <th>Liên hệ</th>
                        </tr>
                        </thead>
                        <tbody>
                        <!-- Lặp qua danh sách feedback -->
                        <c:forEach var="feedback" items="${feedback}">
                            <tr>
                                <td>${feedback.productName}</td>
                                <td>${feedback.cusName}</td>
                                <td>${feedback.content}</td>
                                <td>${feedback.dateCreate}</td>
                                <td style="gap: 5px">${feedback.rating} <i class="fas fa-star" style="color: #ffeb98"></i></td>
                                <td>
                                    <button class="contact-button">Liên hệ</button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </c:if>
        <c:if test="${role == 'admin'}">
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
        </c:if>
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
<div id="editProductOverlay" class="modal-overlay" style="display:none;">
    <div class="modal-content">
        <span class="close-button" onclick="closeEditProductOverlay()">&times;</span>
        <h2 class="modal-title">✏️ Chỉnh sửa sản phẩm</h2>

        <form id="editProductForm">
            <div class="product-detail-container">
                <div class="product-image-wrapper">
                    <div id="editProductImagesDisplay" class="product-images"></div>
                </div>

                <div class="product-info-wrapper">
                    <table class="product-info-table">
                        <tbody>
                        <tr>
                            <th>Mã sản phẩm</th>
                            <td><input type="text" id="editProductId" name="id_product" readonly></td>
                        </tr>
                        <tr>
                            <th>Tên sản phẩm</th>
                            <td><input type="text" id="editProductName" name="name" readonly></td>
                        </tr>
                        <tr>
                            <th>Xuất xứ</th>
                            <td><input type="text" id="editProductOrigin" name="origin" readonly></td>
                        </tr>
                        <tr>
                            <th>Giá</th>
                            <td><input type="number" id="editProductPrice" name="price" readonly></td>
                        </tr>
                        <tr>
                            <th>Đánh giá</th>
                            <td><input type="text" id="editProductRating" name="rating" readonly></td>
                        </tr>
                        <tr>
                            <th>Trạng thái</th>
                            <td>
                                <select id="editProductStatus" name="status" disabled>
                                    <option value="true">Còn hàng</option>
                                    <option value="false">Hết hàng</option>
                                </select>
                                <input type="hidden" id="hiddenStatus" name="status">
                            </td>
                        </tr>
                        <tr>
                            <th>Mô tả</th>
                            <td><textarea id="editProductDescribe" name="describe_1" readonly></textarea></td>
                        </tr>
                        <tr>
                            <th>Số lượng</th>
                            <td><input type="number" id="editProductQuantity" name="quantity" readonly></td>
                        </tr>
                        <tr>
                            <th>Ngày nhập</th>
                            <td><input type="date" id="editProductEntryDate" name="entry_date" readonly></td>
                        </tr>
                        <tr>
                            <th>Hạn sử dụng</th>
                            <td><input type="text" id="editProductShelfLife" name="shelf_life" readonly></td>
                        </tr>
                        <tr>
                            <th>Thời gian bảo hành</th>
                            <td><input type="text" id="editProductWarranty" name="warranty_period" readonly></td>
                        </tr>
                        <tr>
                            <th>Đặc điểm</th>
                            <td><input type="text" id="editProductCharacteristic" name="characteristic" readonly></td>
                        </tr>
                        <tr>
                            <th>Cách bảo quản</th>
                            <td><input type="text" id="editProductPreserve" name="preserve_product" readonly></td>
                        </tr>
                        <tr>
                            <th>Cách sử dụng</th>
                            <td><input type="text" id="editProductUse" name="use_product" readonly></td> <!-- note corrected name -->
                        </tr>
                        <tr>
                            <th>Lợi ích</th>
                            <td><input type="text" id="editProductBenefit" name="benefit" readonly></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div style="margin-top: 1em; text-align: center;">
                <button type="button" id="editEnableBtn">Chỉnh sửa</button>
                <button type="submit" id="editSaveBtn" disabled>Lưu</button>
                <button type="button" onclick="closeEditProductOverlay()">Đóng</button>
            </div>
        </form>
    </div>
</div>
<div id="custom-toast-container"></div>
<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.13.1/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        $('#topCustomersTable, #recent-customers').DataTable({
            paging: true,
            searching: false,
            ordering: true,
            pageLength: 10,
            lengthMenu: [5, 10, 20, 50],
            language: {
                lengthMenu: "Hiển thị _MENU_ dòng mỗi trang",
                info: "Hiển thị từ _START_ đến _END_ của _TOTAL_ dòng",
                paginate: {
                    previous: "Trước",
                    next: "Tiếp"
                }
            }
        });
        // Khởi tạo DataTable cho tất cả các bảng
        $('#feedbackTable, #supplierTable, #customerTable, #productTable, #promotionTable, #orderTable, #userAdmin, #newOrderTable').DataTable({
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
        const fullUrl = "/project_fruit/admin/invoice-detail?id=" + invoice.id;
        console.log("📤 Fetch URL:", fullUrl);

        fetch(fullUrl)
            .then(res => {
                if (!res.ok) throw new Error("Lỗi khi gọi API chi tiết hóa đơn");
                return res.json();
            })
            .then(products => {
                console.log("📦 Sản phẩm nhận được:", products);
                products.forEach((p, index) => {
                    console.log(`🧾 [${index}]`, p);
                });
                const body = document.getElementById("invoiceProductBody");
                body.innerHTML = "";

                if (!products || products.length === 0) {
                    body.innerHTML = `<tr><td colspan="5" style="color:red;">Không có sản phẩm nào.</td></tr>`;
                    return;
                }

                products.forEach((p, index) => {
                    const subtotal = p.quantity * p.price * (1 - (p.discount || 0) / 100);
                    const row = `
        <tr>
            <td>${index + 1}</td>
            <td>${p.name || p.productName || ''}</td>
            <td>${p.quantity}</td>
            <td>${p.price.toLocaleString("vi-VN")} đ</td>
            <td>${subtotal.toLocaleString("vi-VN")} đ</td>
        </tr>
    `;
                    body.innerHTML += row;
                });
            })
            .catch(err => {
                console.error("❌ Lỗi khi fetch chi tiết sản phẩm:", err);
                const body = document.getElementById("invoiceProductBody");
                body.innerHTML = `<tr><td colspan="5" style="color:red;">Không thể tải danh sách sản phẩm.</td></tr>`;
            });
    }
</script>
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

                            showCustomToast(`${actionLabel} đơn hàng #${id} thành công!`);
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
<script>
    function openEditProductOverlay(product) {
        document.getElementById('editProductId').value = product.id_product || '';
        document.getElementById('editProductName').value = product.name || '';
        document.getElementById('editProductOrigin').value = product.origin || '';
        document.getElementById('editProductPrice').value = product.price || '';
        document.getElementById('editProductRating').value = product.rating || '';
        document.getElementById('editProductStatus').value = product.status ? 'true' : 'false';
        document.getElementById('hiddenStatus').value = product.status ? 'true' : 'false';
        document.getElementById('editProductDescribe').value = product.describe_1 || '';
        document.getElementById('editProductQuantity').value = product.quantity || '';
        document.getElementById('editProductEntryDate').value = product.entry_date || '';
        document.getElementById('editProductShelfLife').value = product.shelf_life || '';
        document.getElementById('editProductWarranty').value = product.warranty_period || '';
        document.getElementById('editProductCharacteristic').value = product.characteristic || '';
        document.getElementById('editProductPreserve').value = product.preserve_product || '';
        document.getElementById('editProductUse').value = product.use_product || '';
        document.getElementById('editProductBenefit').value = product.benefit || '';

        // Hiển thị ảnh sản phẩm
        const imagesDiv = document.getElementById('editProductImagesDisplay');
        imagesDiv.innerHTML = '';
        if (product.listImg && Array.isArray(product.listImg)) {
            product.listImg.forEach(img => {
                const imgEl = document.createElement('img');
                imgEl.src = img.url;
                imgEl.alt = product.name || 'Product Image';
                imgEl.style.width = '320px';
                imgEl.style.height = 'auto';
                imgEl.style.borderRadius = '6px';
                imagesDiv.appendChild(imgEl);
            });
        } else {
            imagesDiv.textContent = 'Không có ảnh sản phẩm.';
        }

        disableEditFields(true);
        document.getElementById('editSaveBtn').disabled = true;
        document.getElementById('editEnableBtn').disabled = false;

        document.getElementById('editProductOverlay').style.display = 'flex';
    }

    function disableEditFields(disabled) {
        const form = document.getElementById('editProductForm');
        Array.from(form.elements).forEach(el => {
            if (el.name !== 'id_product') {
                if (el.tagName.toLowerCase() === 'select') {
                    el.disabled = disabled;
                    document.getElementById('hiddenStatus').value = el.value;
                } else {
                    el.readOnly = disabled;
                }
            }
        });
    }

    function closeEditProductOverlay() {
        document.getElementById('editProductOverlay').style.display = 'none';
    }

    // Bật chế độ chỉnh sửa
    document.getElementById('editEnableBtn').addEventListener('click', function () {
        disableEditFields(false);
        this.disabled = true;
        document.getElementById('editSaveBtn').disabled = false;
    });

    // Submit form AJAX
    document.getElementById('editProductForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const form = this;

        // Tạm thời enable tất cả trường để lấy đủ dữ liệu
        const disabledElements = [];
        Array.from(form.elements).forEach(el => {
            if (el.disabled) {
                disabledElements.push(el);
                el.disabled = false;
            }
            if (el.readOnly) {
                disabledElements.push(el);
                el.readOnly = false;
            }
        });

        const formData = new FormData(form);

        // Khôi phục lại trạng thái disabled và readOnly
        disabledElements.forEach(el => {
            if (el.tagName.toLowerCase() === 'select') {
                el.disabled = true;
            } else {
                el.readOnly = true;
            }
        });

        // Đồng bộ giá trị status từ select sang hidden input
        formData.set('status', document.getElementById('editProductStatus').value);

        fetch('admin/edit-product', {
            method: 'POST',
            body: formData
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    closeEditProductOverlay();
                    location.reload();
                } else {
                    alert('Lỗi: ' + data.message);
                }
            })
            .catch(err => alert('Lỗi kết nối: ' + err.message));
    });

    // Gắn sự kiện nút chỉnh sửa trên bảng
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.edit-button').forEach(btn => {
            btn.addEventListener('click', () => {
                const jsonStr = btn.getAttribute('data-product');
                try {
                    const product = JSON.parse(jsonStr);
                    openEditProductOverlay(product);
                } catch (e) {
                    console.error('Lỗi parse JSON:', e);
                }
            });
        });
    });
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        fetch("/project_fruit/admin/revenue-monthly")
            .then(res => res.json())
            .then(data => {
                console.log("Dữ liệu từ servlet:", data);

                const labels = Array.from({ length: 12 }, (_, i) => `Tháng ${i + 1}`);
                const dataset = Array.from({ length: 12 }, (_, i) => data[i + 1] || 0);

                labels.forEach((label, i) => {
                    console.log(`Label[${i}]:`, label);
                });
                console.log("DATASET =", dataset);

                const ctx = document.getElementById("monthlyRevenueChart1").getContext("2d");
                new Chart(ctx, {
                    type: "line",
                    data: {
                        labels: labels,
                        datasets: [{
                            label: "Doanh thu (VND)",
                            data: dataset,
                            borderColor: "#00c5cc",
                            backgroundColor: "rgba(0, 197, 204, 0.1)",
                            fill: true,
                            tension: 0.3,
                            pointRadius: 4,
                            pointHoverRadius: 6
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            x: {
                                type: 'category', // ✅ ép trục X là loại phân loại (không phải số)
                                ticks: {
                                    autoSkip: false,
                                    maxRotation: 0,
                                    minRotation: 0,
                                    font: {
                                        family: 'Arial'
                                    }
                                }
                            },
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    callback: value => value.toLocaleString("vi-VN") + " đ"
                                }
                            }
                        },
                        plugins: {
                            tooltip: {
                                callbacks: {
                                    label: function (ctx) {
                                        return ctx.dataset.label + ": " + ctx.parsed.y.toLocaleString("vi-VN") + " đ";
                                    }
                                }
                            }
                        }
                    }
                });
            })
            .catch(err => console.error("Lỗi khi fetch doanh thu:", err));
    });
</script>
<script>
    fetch("/project_fruit/admin/revenue-top-products")
        .then(res => res.json())
        .then(data => {
            const ctx = document.getElementById("productDonutChart").getContext("2d");
            new Chart(ctx, {
                type: "doughnut",
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        data: Object.values(data),
                        backgroundColor: [
                            "#FF6384", "#36A2EB", "#FFCE56", "#4CAF50", "#E91E63"
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: "bottom"
                        },
                        tooltip: {
                            callbacks: {
                                label: function (ctx) {
                                    return ctx.label + ": " + ctx.raw.toLocaleString("vi-VN") + " đ";
                                }
                            }
                        }
                    }
                }
            });
        });
</script>
<script>
    fetch("/project_fruit/admin/revenue-by-payment-method")
        .then(res => res.json())
        .then(data => {
            const ctx = document.getElementById("paymentMethodRevenueChart").getContext("2d");
            new Chart(ctx, {
                type: "pie",
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: "Doanh thu (VND)",
                        data: Object.values(data),
                        backgroundColor: ["#4CAF50", "#FF9800", "#03A9F4", "#9C27B0"]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label: function (ctx) {
                                    const label = ctx.label || "";
                                    const value = ctx.raw || 0;
                                    return `${label}: ${value.toLocaleString("vi-VN")} đ`;
                                }
                            }
                        },
                        legend: {
                            position: "bottom"
                        }
                    }
                }
            });
        });
</script>
<script>
    fetch("/project_fruit/admin/order-status-month")
        .then(res => res.json())
        .then(data => {
            const ctx = document.getElementById("orderStatusChart").getContext("2d");
            new Chart(ctx, {
                type: "bar",
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: "Số đơn hàng",
                        data: Object.values(data),
                        backgroundColor: ["#4CAF50", "#FF9800", "#F44336"]
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                stepSize: 1
                            }
                        }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: (ctx) => `${ctx.dataset.label}: ${ctx.raw} đơn`
                            }
                        }
                    }
                }
            });
        });
</script>
<script>
    fetch("/project_fruit/admin/dashboard-summary")
        .then(res => res.json())
        .then(data => {
            document.getElementById("summary-revenue").textContent =
                Number(data.totalRevenue || 0).toLocaleString("vi-VN") + " đ";
            document.getElementById("summary-orders").textContent = data.totalOrders || 0;
            document.getElementById("summary-canceled").textContent = data.canceledOrders || 0;
        });
</script>
</body>

</html>