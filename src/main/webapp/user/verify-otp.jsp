<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/register.css">
  <link href='https://cdn.jsdelivr.net/npm/boxicons@2.0.5/css/boxicons.min.css' rel='stylesheet'>
  <title>Xác thực OTP - VitaminFruit</title>
</head>

<body>
<div class="l-form">
  <div class="shape1"></div>
  <div class="shape2"></div>

  <div class="form">
    <img src="${pageContext.request.contextPath}/assets/img/authentication.svg" alt="Background">

    <!-- Form gửi OTP đến VerifyOtpServlet -->
    <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="form__content">
      <h1 class="form__title">Xác thực OTP</h1>

      <!-- Email đã đăng ký -->
      <div class="form__div">
        <div class="form__icon">
          <i class='bx bx-envelope'></i>
        </div>
        <div class="form__div-input">
          <label for="email" class="form__label">Email</label>
          <input type="email" id="email" name="email" class="form__input" required value="${email}">
        </div>
      </div>

      <!-- Nhập mã OTP -->
      <div class="form__div">
        <div class="form__icon">
          <i class='bx bx-key'></i>
        </div>
        <div class="form__div-input">
          <label for="otp" class="form__label">Mã OTP</label>
          <input type="text" id="otp" name="otp" class="form__input" required>
        </div>
      </div>

      <!-- Nút xác thực -->
      <input type="submit" class="form__button" value="Xác thực">

      <!-- Hiển thị thông báo -->
      <c:if test="${not empty message}">
        <p style="color: red; text-align: center;">${message}</p>
      </c:if>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/formlogin.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>

</html>
