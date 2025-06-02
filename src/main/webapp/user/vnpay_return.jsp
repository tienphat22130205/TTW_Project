<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Kết quả thanh toán</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 30px;
            text-align: center;
            background: #f9f9f9;
            color: #333;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            background: white;
            padding: 40px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgb(0 0 0 / 0.1);
        }
        .icon-success {
            margin-bottom: 20px;
            color: #7ca55c;
            font-size: 80px;
        }
        h2 {
            color: #7ca55c;
            font-weight: 700;
            margin-bottom: 10px;
        }
        p.desc {
            color: #555;
            font-size: 14px;
            margin-bottom: 30px;
        }
        .buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-bottom: 20px;
        }
        .btn {
            background-color: #c37348;
            color: white;
            font-weight: 600;
            border: none;
            padding: 12px 25px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            user-select: none;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #a35b33;
        }
        .countdown {
            font-size: 14px;
            color: #888;
        }
    </style>
    <script>
        // Đếm ngược 7 giây rồi chuyển về trang chủ
        let timeLeft = 7;
        function countdown() {
            if(timeLeft <= 0) {
                window.location.href = "${pageContext.request.contextPath}/home";
            } else {
                document.getElementById('countdown').innerText = timeLeft + " giây";
                timeLeft--;
                setTimeout(countdown, 1000);
            }
        }

        function printReceipt() {
            window.print();
        }

        window.onload = countdown;
    </script>
</head>
<body>
<div class="container">
    <div class="icon-success">
        <i class="fas fa-check-circle"></i>
    </div>
    <h2>Thanh toán thành công</h2>
    <p class="desc">Vui lòng lưu biên lai để xuất trình khi nhận kết quả hồ sơ tại cơ quan chức năng</p>

    <div class="buttons">
        <a href="${pageContext.request.contextPath}/history" class="btn">LỊCH SỬ GIAO DỊCH</a>
        <a href="${pageContext.request.contextPath}/receipt?orderId=${param.vnp_TxnRef}" target="_blank" class="btn">
            TẢI BIÊN LAI
        </a>
    </div>

    <div class="countdown">
        Bạn sẽ được chuyển về trang chủ sau <span id="countdown">7 giây</span>.
    </div>
</div>
</body>
</html>
