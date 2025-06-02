<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Kết quả thanh toán</title>
    <meta http-equiv="refresh" content="5;URL=${pageContext.request.contextPath}/home" />
    <style>
        body { font-family: Arial, sans-serif; padding: 30px; text-align: center; }
        .success { color: green; font-size: 20px; }
        .error { color: red; font-size: 20px; }
    </style>
</head>
<body>
<h2>Kết quả thanh toán từ VNPAY</h2>
<p>Mã đơn hàng: <strong>${param.vnp_TxnRef}</strong></p>
<p>Trạng thái giao dịch: <strong>${param.vnp_TransactionStatus}</strong></p>
<c:choose>
    <c:when test="${param.vnp_ResponseCode == '00' && param.vnp_TransactionStatus == '00'}">
        <p class="success">✅ Giao dịch thành công!</p>
    </c:when>
    <c:otherwise>
        <p class="error">❌ Giao dịch thất bại hoặc bị hủy.</p>
    </c:otherwise>
</c:choose>

<p>Bạn sẽ được chuyển hướng về trang chủ sau vài giây...</p>

<script>
    setTimeout(function () {
        window.location.href = "${pageContext.request.contextPath}/home";
    }, 2000); // 2000ms = 2 giây
</script>
</body>
</html>

