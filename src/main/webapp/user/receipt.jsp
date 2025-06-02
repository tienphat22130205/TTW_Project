<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Biên lai thanh toán</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 30px auto;
            padding: 20px;
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            color: #7ca55c;
            margin-bottom: 30px;
        }
        .info {
            margin-bottom: 20px;
        }
        .info strong {
            display: inline-block;
            width: 150px;
            color: #444;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            margin-bottom: 30px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #f8f8f8;
            color: #333;
        }
        .total-row td {
            font-weight: bold;
            font-size: 16px;
            text-align: right;
            padding-right: 20px;
        }
        .button-group {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 20px;
        }
        .btn-print , .btn-home{
            display: block;
            margin: 0 auto;
            padding: 12px 30px;
            background-color: #c37348;
            color: white;
            border: none;
            border-radius: 6px;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            user-select: none;
            transition: background-color 0.3s ease;
        }
        .btn-print:hover,
        .btn-home:hover {
            background-color: #a35b33;
        }
    </style>
    <script>
        function printReceipt() {
            window.print();
        }
        function goHome() {
            window.location.href = "${pageContext.request.contextPath}/home";
        }
    </script>
</head>
<body>
<h2>Biên lai thanh toán</h2>

<div class="info">
    <p><strong>Mã hóa đơn:</strong> ${invoice.idInvoice}</p>
    <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${invoice.createDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
    <p><strong>Người đặt:</strong> ${invoice.accountName}</p>
    <p><strong>Người nhận:</strong> ${invoice.receiverName}</p>
    <p><strong>Điện thoại:</strong> ${invoice.phone}</p>
    <p><strong>Email:</strong> ${invoice.email}</p>
    <p><strong>Địa chỉ:</strong> ${invoice.addressFull}</p>
    <p><strong>Phương thức thanh toán:</strong> ${invoice.paymentMethod}</p>
    <p><strong>Trạng thái đơn hàng:</strong> ${invoice.orderStatus}</p>
</div>

<table>
    <thead>
    <tr>
        <th>Tên sản phẩm</th>
        <th>Số lượng</th>
        <th>Đơn giá</th>
        <th>Giảm giá (%)</th>
        <th>Thành tiền</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${invoiceDetails}">
        <tr>
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/></td>
            <td><fmt:formatNumber value="${item.discount}" type="number" /></td>
            <td>
                <fmt:formatNumber
                        value="${item.price * item.quantity * (1 - item.discount / 100)}"
                        type="currency" currencySymbol="₫"/>
            </td>
        </tr>
    </c:forEach>
    <tr class="total-row">
        <td colspan="4" style="text-align:right;">Phí vận chuyển:</td>
        <td><fmt:formatNumber value="${invoice.shippingFee}" type="currency" currencySymbol="₫"/></td>
    </tr>
    <tr class="total-row">
        <td colspan="4" style="text-align:right;">Tổng thanh toán:</td>
        <td><fmt:formatNumber value="${invoice.totalPrice}" type="currency" currencySymbol="₫"/></td>
    </tr>
    </tbody>
</table>
<div class="button-group">
    <button class="btn-print" onclick="printReceipt()">In biên lai</button>
    <button class="btn-home" onclick="goHome()">Quay về trang chủ</button>
</div>
</body>
</html>
