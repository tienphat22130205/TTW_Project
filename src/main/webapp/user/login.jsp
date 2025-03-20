<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- ===== CSS ===== -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">

    <!-- ===== BOX ICONS ===== -->
    <link href='https://cdn.jsdelivr.net/npm/boxicons@2.0.5/css/boxicons.min.css' rel='stylesheet'>

    <title>Login - VitaminFruit</title>
</head>
<body>
<%--<form method="post" action="login">--%>
<div class="l-form">
    <div class="shape1"></div>
    <div class="shape2"></div>

    <div class="form">
        <img src="${pageContext.request.contextPath}/assets/img/authentication.svg" alt="Background 1">

        <form action="${pageContext.request.contextPath}/login" method="POST" class="form__content">
            <h1 class="form__title">VitaminFruit</h1>
            <div class="form__div form__div-one">
                <div class="form__icon">
                    <i class='bx bx-user-circle'></i>
                </div>

                <div class="form__div-input">
                    <label for="" class="form__label">Email</label>
                    <!-- Giữ lại email nếu đăng nhập sai -->
                    <input type="text" id="useremail" name="useremail" class="form__input"
                           value="${param.useremail != null ? param.useremail : ''}" required>
                </div>
            </div>

            <div class="form__div">
                <div class="form__icon">
                    <i class='bx bx-lock' ></i>
                </div>

                <div class="form__div-input">
                    <label for="" class="form__label">Mật khẩu</label>
                    <!-- Mật khẩu không được giữ lại khi đăng nhập sai -->
                    <input type="password" id="pass" name="pass" class="form__input" required>
                </div>
            </div>

            <!-- Thông báo lỗi đăng nhập -->
            <c:if test="${not empty errorMessage}">
                <p style="color: red; text-align: center;">${errorMessage}</p>
            </c:if>

            <a href="${pageContext.request.contextPath}/user/forgotPassword.jsp" class="form__forgot">Quên mật khẩu ?</a>

            <input type="submit" class="form__button" value="Đăng nhập">

            <div class="register">
                <p>Chưa có tài khoản?</p>
                <a href="${pageContext.request.contextPath}/user/register.jsp">Đăng Kí</a>
            </div>
            <input type="hidden" id="googleToken" name="googleToken" value="">
            <input type="hidden" id="facebookToken" name="facebookToken" value="">
            <div class="form__social">
                <span class="form__social-text">Đăng nhập bằng</span>

                <a href="#" class="form__social-icon" onclick="signInWithFacebook();"><i class='bx bxl-facebook' ></i></a>
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid

&redirect_uri=http://localhost:9090/project_fruit/google-login

&response_type=code

&client_id=1033832143997-6e97eqtcre3a5em76s2fo28rld9dpf8v.apps.googleusercontent.com

&approval_prompt=force" class="form__social-icon" onclick="signInWithGoogle();"><i class='bx bxl-google' ></i></a>
                <a href="#" class="form__social-icon"><i class='bx bxl-instagram' ></i></a>
            </div>
        </form>
    </div>

</div>
<%--</form>--%>

<!-- ===== MAIN JS ===== -->
<script src="${pageContext.request.contextPath}/assets/js/formlogin.js"></script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="https://connect.facebook.net/en_US/sdk.js"></script>
<script>
    // Google Sign-In
    function signInWithGoogle() {
        gapi.load('auth2', function() {
            gapi.auth2.init({
                client_id: 'YOUR_GOOGLE_CLIENT_ID' // Thay thế bằng Client ID của bạn
            }).then(function() {
                gapi.auth2.getAuthInstance().signIn().then(function(googleUser) {
                    var idToken = googleUser.getAuthResponse().id_token;
                    document.getElementById('googleToken').value = idToken;
                    document.querySelector('form').submit();
                });
            });
        });
    }

    // Facebook Login
    window.fbAsyncInit = function() {
        FB.init({
            appId: 'YOUR_FACEBOOK_APP_ID', // Thay thế bằng App ID của bạn
            cookie: true,
            xfbml: true,
            version: 'v19.0'
        });

        FB.AppEvents.logPageView();

    };

    function signInWithFacebook() {
        FB.login(function(response) {
            if (response.authResponse) {
                var accessToken = response.authResponse.accessToken;
                document.getElementById('facebookToken').value = accessToken;
                document.querySelector('form').submit();
            } else {
                console.log('User cancelled login or did not fully authorize.');
            }
        }, {scope: 'email'});
    }
</script>
</body>
</html>
