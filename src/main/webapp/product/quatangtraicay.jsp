<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "f" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!-- link swiper -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <!-- link style css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <!-- Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <!-- link logo anh -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/logoBank/logoweb.png"  type="image/x-icon">
    <title>Selling Fruit</title>
</head>

<body>
<!-- header section stars -->
<header>
    <div class="header-container">
        <div class="left">
            <div class="menu">
          <span id="menuToggle" onclick="toggleSidebarMenu()">
            <i id="menuIcon" class="fa-solid fa-bars"></i></span>
            </div>
            <div class="logo">
                <a href="/index.jsp">
                    <h1>Vitamin<span>FRUIT</span></h1>
                </a>
            </div>
            <div class="search" style="position: relative;">
                <form action="${pageContext.request.contextPath}/search-result" method="GET" id="searchForm">
                    <input type="text" id="search-input" placeholder="Tìm kiếm sản phẩm..." name="keyword"
                           oninput="fetchSuggestions(this.value)">
                    <button type="submit" class="search-btn">
                        <i class="fa-solid fa-search"></i>
                    </button>
                </form>
                <div id="search-results" class="search-results"></div>
            </div>
        </div>
        <div class="center">
            <div class="delivery" onclick="toggleBranchSelection()">
                <span>Giao hoặc đến lấy tại ▼</span>
                <p>Chi nhánh 1 - 43 Nguyễn Thái Học...</p>
            </div>
        </div>
        <div class="right">
            <div class="hotline">
                <i class="fas fa-phone"></i>
                <span>Hotline: 0865660775</span>
            </div>
            <!-- chuong thong bao -->
            <div class="notification-icon" id="notificationBell_Menu">
                <a href="#" id="notificationToggle" class="notification-link" style="color: #FFFFFF">
                    <i class="fas fa-bell"></i>
                    <span class="notification-label">Thông báo</span>
                    <span class="notification-count" id="notificationCount">0</span>
                </a>
            </div>
            <div class="notification-popup" id="notificationPopup">
                <header style="font-size: 20px">Thông báo mới</header>
                <table class="notification-table">
                    <tbody>
                    <!-- Dòng thông báo sẽ được chèn vào đây -->
                    </tbody>
                </table>
            </div>
            <style>
                /* Icon chuông thông báo */
                .notification-icon {
                    position: relative;  /* cần để z-index hoạt động */
                    z-index: 99999;      /* cao hơn các phần tử khác */
                    display: flex;
                    font-size: 25px;
                    user-select: none;
                    cursor: pointer;     /* con trỏ tay khi hover */
                }

                /* Link chứa icon + label */
                .notification-link {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    color: #FFFFFF;
                    text-decoration: none;
                    gap: 2px;
                    cursor: pointer;
                    position: relative;
                }

                /* Nhãn thông báo */
                .notification-label {
                    font-size: 14px;
                    white-space: nowrap;
                    display: flex;
                    align-items: center;
                }

                /* Số lượng thông báo chưa xem */
                .notification-count {
                    position: absolute;
                    top: -5px;
                    right: -10px;
                    background-color: red;
                    color: white;
                    border-radius: 50%;
                    padding: 2px 6px;
                    font-size: 12px;
                    font-weight: bold;
                    user-select: none;
                }

                /* Hiệu ứng rung khi hover chuông */
                .notification-link:hover i.fas.fa-bell {
                    animation: shake 0.5s ease-in-out;
                }

                @keyframes shake {
                    0%, 100% {
                        transform: translateX(0);
                    }
                    20%, 60% {
                        transform: translateX(-5px);
                    }
                    40%, 80% {
                        transform: translateX(5px);
                    }
                }

                /* Popup thông báo */
                .notification-popup {
                    position: absolute;
                    right: 0;
                    top: 68px;
                    margin-top: 4px;
                    width: 430px;
                    height: 500px;
                    overflow-y: auto;
                    background-color: white;
                    color: black;
                    border-radius: 6px;
                    box-shadow: 0 4px 10px rgb(0 0 0 / 0.3);
                    z-index: 9999;

                    opacity: 0;
                    pointer-events: none;
                    transform: translateX(30px);
                    transition: opacity 0.3s ease, transform 0.3s ease;
                }

                .notification-popup.active {
                    display: block;
                    opacity: 1;
                    pointer-events: auto;
                    transform: translateX(0);
                }

                /* Tiêu đề popup */
                .notification-popup header {
                    padding: 10px;
                    font-weight: bold;
                    font-size: 20px;
                    border-bottom: 1px solid #ddd;
                }

                /* Bảng trong popup */
                .notification-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin: 0;
                    padding: 10px;
                    font-size: 14px;
                }

                .notification-table tbody tr {
                    border-bottom: 1px solid #eee;
                }

                .notification-table tbody tr:last-child {
                    border-bottom: none;
                }

                .notification-table tbody tr td {
                    padding: 8px 10px;
                }

                /* Khi không có thông báo */
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
                const tbody = popup.querySelector('tbody');

                async function loadNotifications() {
                    try {
                        const response = await fetch('/project_fruit/notifications');
                        if (!response.ok) throw new Error('Lỗi tải thông báo');
                        const notifications = await response.json();

                        tbody.innerHTML = '';

                        if (!notifications || notifications.length === 0) {
                            const tr = document.createElement('tr');
                            const td = document.createElement('td');
                            td.classList.add('no-notification');
                            td.textContent = 'Không có thông báo mới';
                            td.colSpan = 1;
                            tr.appendChild(td);
                            tbody.appendChild(tr);

                            countSpan.style.display = "none";
                            countSpan.textContent = "";
                        } else {
                            notifications.forEach(text => {
                                const tr = document.createElement('tr');
                                const td = document.createElement('td');
                                td.textContent = text;
                                tr.appendChild(td);
                                tbody.appendChild(tr);
                            });
                            countSpan.style.display = "inline-block";
                            countSpan.textContent = notifications.length;
                        }
                    } catch (error) {
                        console.error('Lỗi khi lấy thông báo:', error);
                    }
                }

                bell.addEventListener("click", async (event) => {
                    event.preventDefault();
                    popup.classList.toggle("active");

                    if (popup.classList.contains("active")) {
                        try {
                            const res = await fetch('/project_fruit/notifications/mark-seen', { method: 'POST' });
                            if (res.ok) {
                                countSpan.style.display = "none";
                                countSpan.textContent = "";
                                // Giữ nguyên nội dung popup hiện tại, không gọi loadNotifications() lại
                            }
                        } catch (error) {
                            console.error('Lỗi đánh dấu đã xem:', error);
                        }
                    }
                });

                document.addEventListener("click", (event) => {
                    if (!bell.contains(event.target) && !popup.contains(event.target)) {
                        popup.classList.remove("active");
                    }
                });

                document.addEventListener('DOMContentLoaded', loadNotifications);

            </script>
            <!-- chuong thong bao -->
            <div class="cart">
                <a href="${pageContext.request.contextPath}/show-cart" style="color: white">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <span>Giỏ hàng</span>
                    <span class="cart-badge">${sessionScope.cart != null ? sessionScope.cart.getTotalQuantity() : 0}</span>
                </a>
            </div>
            <div class="account">
                <!-- Kiểm tra nếu người dùng đã đăng nhập -->
                <c:if test="${not empty sessionScope.user}">
                    <!-- Nếu người dùng đã đăng nhập, hiển thị avatar và thông tin -->
                    <a href="${pageContext.request.contextPath}/user/user.jsp">
                        <img src="${pageContext.request.contextPath}/assets/img/anhdaidien.jpg" alt="Avatar"
                             class="avatar" onclick="toggleUserMenu()">
                    </a>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <!-- Nếu chưa đăng nhập, hiển thị icon tài khoản -->
                    <a href="${pageContext.request.contextPath}/user/login.jsp" style="color: white">
                        <i class="fa-solid fa-user"></i>
                        <span>Tài khoản</span>
                    </a>
                </c:if>
            </div>
        </div>
    </div>
    <!-- Branch Selection Form -->
    <div class="branch-selection" id="branchSelection">
        <h2>KHU VỰC MUA HÀNG</h2>
        <div class="form-group">
            <label for="city">Tỉnh/Thành</label>
            <select id="city">
                <option>- Chọn Thành phố/ tỉnh -</option>
                <option>Hồ Chí Minh</option>
                <!-- Thêm các tỉnh/thành khác nếu cần -->
            </select>
        </div>
        <div class="form-group">
            <label for="district">Quận/Huyện</label>
            <select id="district">
                <option>- Chọn Quận/Huyện -</option>
                <option>- Thủ Đức -</option>
                <option>- Quận 1 -</option>
                <!-- Thêm các quận/huyện khác nếu cần -->
            </select>
        </div>

        <div class="selected-branch">
            <p>Giao hoặc đến lấy tại:</p>
            <div class="branch-info-highlight">
                <p><strong>Chi nhánh 1 - 43 Nguyễn Thái Học, Phường Cầu Ông Lãnh, Quận 1</strong></p>
            </div>
        </div>

        <p>Chọn cửa hàng gần bạn nhất để tối ưu chi phí giao hàng. Hoặc đến lấy hàng</p>

        <div class="branch-list">
            <div class="branch">
                <p><i class="fas fa-map-marker-alt"></i> Chi nhánh 1</p>
                <p>43 Nguyễn Thái Học, Phường Cầu Ông Lãnh, Quận 1</p>
            </div>
            <div class="branch">
                <p><i class="fas fa-map-marker-alt"></i> Chi nhánh 2</p>
                <p>SAV.7-00.01, Tầng trệt Tháp 7, The Sun Avenue, 28 Mai Chí Thọ, phường An Phú, thành phố Thủ Đức,
                    Phường An
                    Phú, Thành phố Thủ Đức</p>
            </div>
        </div>
    </div>
    <!-- User Menu (ẩn khi chưa đăng nhập) -->
    <%--    <div class="user-menu" id="userMenu" style="display: none;">--%>
    <%--        <p>Xin chào, <span id="userNameDisplay">${sessionScope.user.email}</span></p>--%>
    <%--        <ul>--%>
    <%--            <li><a href="${pageContext.request.contextPath}/user/user.jsp"><i class="fas fa-box"></i> Thông tin cá nhân</a></li>--%>
    <%--            <li><a href="#"><i class="fas fa-eye"></i> Đã xem gần đây</a></li>--%>
    <%--            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a></li>--%>
    <%--        </ul>--%>
    <%--    </div>--%>
</header>
<!-- Menu Bar dưới Header -->
<!-- Menu Bar dưới Header -->
<nav class="menu-bar">
    <ul>
        <li><a href="/project_fruit/home" onclick="setActive(this)"><i class="fas fa-home"></i> Trang chủ</a></li>
        <li><a href="/project_fruit/home?category=traicayhomnay" onclick="setActive(this)">Trái ngon hôm nay</a></li>
        <li><a href="/project_fruit/home?category=traicayvietnam" onclick="setActive(this)">Trái cây Việt Nam</a></li>
        <li><a href="/project_fruit/home?category=traicaynhapkhau" onclick="setActive(this)">Trái cây nhập khẩu</a></li>
        <li><a href="/project_fruit/home?category=traicaycatsan" onclick="setActive(this)">Trái cây cắt sẵn</a></li>
        <li  class="active"><a href="/project_fruit/home?category=quatangtraicay" onclick="setActive(this)">Quà tặng trái cây</a></li>
        <li><a href="/project_fruit/home?category=hopquanguyencat" onclick="setActive(this)">Hộp quà Nguyệt Cát</a></li>
        <li><a href="/project_fruit/home?category=traicaysaykho" onclick="setActive(this)">Trái cây sấy khô</a></li>
        <li><a href="/project_fruit/home?category=muttraicay" onclick="setActive(this)">Mứt trái cây</a></li>
        <li><a href="/project_fruit/user/contact.jsp" onclick="setActive(this)">Liên hệ</a></li>
    </ul>
</nav>
<!-- Menu docj ban đầu ẩn , chỉ xuất hiện khi ấn icon -->
<nav class="sidebar-menu" id="sidebarMenu">
    <ul>
        <li class="active"><a href="/project_fruit/home" onclick="setActive(this)"><i class="fas fa-home"></i> Trang chủ</a></li>
        <li><a href="/project_fruit/home?category=traicayhomnay" onclick="setActive(this)">Trái ngon hôm nay</a></li>
        <li><a href="/project_fruit/home?category=traicayvietnam" onclick="setActive(this)">Trái cây Việt Nam</a></li>
        <li><a href="/project_fruit/home?category=traicaynhapkhau" onclick="setActive(this)">Trái cây nhập khẩu</a></li>
        <li><a href="/project_fruit/home?category=traicaycatsan" onclick="setActive(this)">Trái cây cắt sẵn</a></li>
        <li><a href="/project_fruit/home?category=quatangtraicay" onclick="setActive(this)">Quà tặng trái cây</a></li>
        <li><a href="/project_fruit/home?category=hopquanguyencat" onclick="setActive(this)">Hộp quà Nguyệt Cát</a></li>
        <li><a href="/project_fruit/home?category=traicaysaykho" onclick="setActive(this)">Trái cây sấy khô</a></li>
        <li><a href="/project_fruit/home?category=muttraicay" onclick="setActive(this)">Mứt trái cây</a></li>
        <li><a href="/project_fruit/user/contact.jsp" onclick="setActive(this)">Liên hệ</a></li>
    </ul>
</nav>
<!-- header section ends -->

<!-- home section starts -->
<section class="home" id="home">
    <div class="swiper-container background-slider">
        <div class="swiper-wrapper">
            <div class="swiper-slide"><img src="${pageContext.request.contextPath}/assets/img/back3.webp" alt="Background 1"></div>
        </div>
    </div>
</section>

<!-- home section ends -->
<!-- products section starts-->
<!-------------------------------------------------------- quà tặng trái cây  -------------------------------------->
<section class="products" id="products">
    <h1 class="heading"><span>quà tặng trái cây</span></h1>
    <div class="sale_sort">
        <div class="sale">
            <div class="discount-text">Giảm Giá Sốc Đến 25%</div>
            <div class="timer">
                <div>3<br />Giờ</div>
                <div>0<br />Phút</div>
                <div>0<br />Giây</div>
            </div>
        </div>
               <div class="sort">
            <div class="sort-menu">
                <select id="sort-options" onchange="submitSortOption()">
                    <option value="#">Sắp xếp</option>
                    <option value="date">Sản phẩm nổi bật</option>
                    <option value="price_asc">Giá: Tăng dần</option>
                    <option value="price_desc">Giá: Giảm dần</option>
                </select>
                <div id="loading" style="display: none;">Đang tải...</div>
            </div>
        </div>
    </div>
    <c:forEach var="productGroup" items="${productGroups}">
        <div class="swiper product-slider">
            <div class="swiper-wrapper">
                <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                <c:forEach var="product" items="${productGroup}">
                    <div class="swiper-slide box">
                        <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                            <!-- Hiển thị hình ảnh sản phẩm -->
                            <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                 alt="${product.name}" />
                            <!-- Phần giảm giá -->
                            <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                            <!-- Hiển thị thông tin sản phẩm -->
                            <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                            <h3>${product.name}</h3>
                            <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                            <div class="stars">
                                <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                            </div>
                            <!-- Nút thêm vào giỏ hàng -->
                            <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
    <c:forEach var="productGroup" items="${productGroups}">
        <div class="swiper product-slider">
            <div class="swiper-wrapper">
                <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                <c:forEach var="product" items="${productGroup}">
                    <div class="swiper-slide box">
                        <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                            <!-- Hiển thị hình ảnh sản phẩm -->
                            <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                 alt="${product.name}" />
                            <!-- Phần giảm giá -->
                            <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                            <!-- Hiển thị thông tin sản phẩm -->
                            <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                            <h3>${product.name}</h3>
                            <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                            <div class="stars">
                                <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                            </div>
                            <!-- Nút thêm vào giỏ hàng -->
                            <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
    <c:forEach var="productGroup" items="${productGroups}">
        <div class="swiper product-slider">
            <div class="swiper-wrapper">
                <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                <c:forEach var="product" items="${productGroup}">
                    <div class="swiper-slide box">
                        <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                            <!-- Hiển thị hình ảnh sản phẩm -->
                            <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                 alt="${product.name}" />
                            <!-- Phần giảm giá -->
                            <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                            <!-- Hiển thị thông tin sản phẩm -->
                            <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                            <h3>${product.name}</h3>
                            <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                            <div class="stars">
                                <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                            </div>
                            <!-- Nút thêm vào giỏ hàng -->
                            <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
    <c:forEach var="productGroup" items="${productGroups}">
        <div class="swiper product-slider">
            <div class="swiper-wrapper">
                <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                <c:forEach var="product" items="${productGroup}">
                    <div class="swiper-slide box">
                        <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                            <!-- Hiển thị hình ảnh sản phẩm -->
                            <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                 alt="${product.name}" />
                            <!-- Phần giảm giá -->
                            <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                            <!-- Hiển thị thông tin sản phẩm -->
                            <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                            <h3>${product.name}</h3>
                            <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                            <div class="stars">
                                <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                            </div>
                            <!-- Nút thêm vào giỏ hàng -->
                            <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
    <div class="swiper product-slider hidden" id="product-slider-1">
        <c:forEach var="productGroup" items="${productGroups}">
            <div class="swiper product-slider">
                <div class="swiper-wrapper">
                    <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                    <c:forEach var="product" items="${productGroup}">
                        <div class="swiper-slide box">
                            <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                                <!-- Hiển thị hình ảnh sản phẩm -->
                                <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                     alt="${product.name}" />
                                <!-- Phần giảm giá -->
                                <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                                <!-- Hiển thị thông tin sản phẩm -->
                                <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                                <h3>${product.name}</h3>
                                <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                                <div class="stars">
                                    <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                                </div>
                                <!-- Nút thêm vào giỏ hàng -->
                                <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="swiper product-slider hidden" id="product-slider-2">
        <c:forEach var="productGroup" items="${productGroups}">
            <div class="swiper product-slider">
                <div class="swiper-wrapper">
                    <!-- Hiển thị sản phẩm trong mỗi nhóm -->
                    <c:forEach var="product" items="${productGroup}">
                        <div class="swiper-slide box">
                            <a href="${pageContext.request.contextPath}/product-detail?pid=${product.id_product}">
                                <!-- Hiển thị hình ảnh sản phẩm -->
                                <img src="${product.imageUrl != null ? product.imageUrl : '/assets/img/default.jpg'}"
                                     alt="${product.name}" />
                                <!-- Phần giảm giá -->
                                <div class="discount">${product.percentDiscount != null ? product.percentDiscount : 0}%</div>
                                <!-- Hiển thị thông tin sản phẩm -->
                                <h4 style="color: red">Mã sản phẩm: ${product.id_product}</h4>
                                <h3>${product.name}</h3>
                                <h3 class="price">${product.discountedPrice}đ/ <span style="color: gray; text-decoration: line-through"><del>${product.price}đ</del></span></h3>
                                <div class="stars">
                                    <i>Đánh giá: ${product.rating} <i class="fas fa-star"></i></i>
                                </div>
                                <!-- Nút thêm vào giỏ hàng -->
                                <a href="${pageContext.request.contextPath}/add-cart?addToCartPid=${product.id_product}" class="btn">Thêm vào giỏ hàng</a>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="view-more-container">
        <a href="#" class="view-more" id="view-more-btn">Xem thêm sản phẩm</a>
    </div>
</section>
<!-- products section end-->
<!-- footer section star -------------------------------------------------------------->
<section class="footer">
    <div class="box-container">
        <div class="box">
            <h3>VitaminFruit</h3>
            <p>
                "Chăm sóc sức khỏe bạn từ thiên nhiên! VitaminFruit – nguồn dinh dưỡng hoàn hảo cho cơ thể và tâm trí."
            </p>
            <div class="share">
                <a href="#" class="fab fa-facebook-f"></a>
                <a href="#" class="fab fa-twitter"></a>
                <a href="#" class="fab fa-instagram"></a>
                <a href="#" class="fab fa-youtube"></a>
                <a href="#" class="fab fa-tiktok"></a>
            </div>
        </div>
        <div class="box">
            <h3>liên hệ</h3>
            <p>Liên hệ chúng tôi tại đây :</p>
            <a href="#" class="links"><i class="fas fa-phone"></i>+334 286 049</a>
            <a href="#" class="links"><i class="fas fa-phone"></i>+263 463 463</a>
            <a href="#" class="links"><i class="fas fa-envelope"></i>nhom55ltw@gmail.com</a>
            <a href="#" class="links"><i class="fas fa-map-marker-alt"></i>VQCR+GP6, khu phố 6, Thủ Đức,
                Hồ Chí Minh</a>
        </div>
        <div class="box">
            <h3>Hỗ trợ khách hàng</h3>
            <p>
                Luôn hổ trợ khách hàng mọi lúc mọi nơi.
            </p>
            <a href="#home" class="links"><i class="fas fa-arrow-right"></i>Chính sách thương hiệu</a>
            <a href="#features" class="links"><i class="fas fa-arrow-right"></i>Chính sách thành viên</a>
            <a href="#products" class="links"><i class="fas fa-arrow-right"></i>Chính sách kiểm hàng</a>
            <a href="#categories" class="links"><i class="fas fa-arrow-right"></i>Chính sách giao hàng</a>
            <a href="#review" class="links"><i class="fas fa-arrow-right"></i>Chính sách thanh toán</a>
            <a href="#blogs" class="links"><i class="fas fa-arrow-right"></i>Chính sách bảo mật</a>
        </div>
        <div class="box">
            <h3>Đơn vị vận chuyển</h3>
            <p>"Chúng tôi sử dụng các đơn vị vận chuyển uy tín như Grab, Giao Hàng Tiết Kiệm, VNPost và nhiều đơn vị khác."</p>
            <div class="shipping-brands">
                <img src="../assets/img/logoBank/grab.jpg" alt="Grab" />
                <img src="../assets/img/logoBank/giaohangtietkiem.png" alt="Giao Hàng Tiết Kiệm" />
                <img src="../assets/img/logoBank/vnpost.webp" alt="VNPost" />
            </div>
        </div>
    </div>
    <div class="credit">Copyright © 2024 <span>Nhom 55 - Trái Cây Chất Lượng Cao</span></div>
</section>
<!-- footer section end -->

<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/assets/js/fruit.js" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/login.js"></script>
<script>
    // Hàm gửi yêu cầu sắp xếp sản phẩm khi người dùng chọn lựa chọn trong dropdown
    function submitSortOption() {
        const sortOption = document.getElementById('sort-options').value;
        const url = new URL(window.location.href);
        url.searchParams.set('sort', sortOption); // Thêm tham số sắp xếp vào URL
        window.location.href = url.toString(); // Tải lại trang với tham số sắp xếp
    }
</script>
</body>

</html>

