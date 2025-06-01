function showSection(sectionId, title) {
    // Thay đổi tiêu đề
    document.getElementById('page-title').innerHTML = `
        <label for="nav-toggle">
            <span class="fa-solid fa-bars"></span>
        </label> ${title}`;

    // Ẩn tất cả các phần
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => {
        section.classList.remove('active');
    });

    // Hiển thị phần được chọn
    const selectedSection = document.getElementById(sectionId);
    if (selectedSection) {
        selectedSection.classList.add('active');
    }

    // Xóa class active từ tất cả các mục menu
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => item.classList.remove('active'));

    // Thêm class active vào mục menu được chọn
    const activeMenuItem = document.querySelector(`a[onclick="showSection('${sectionId}', '${title}')"]`);
    if (activeMenuItem) {
        activeMenuItem.classList.add('active');
    }
}

// Mặc định hiển thị phần dashboard khi tải trang
document.addEventListener('DOMContentLoaded', () => {
    showSection('dashboard', 'Dashboard');
});
// dong mo sidebar
document.addEventListener("DOMContentLoaded", function () {
    document.querySelector("#nav-toggle").addEventListener("change", function () {
        document.querySelector(".sidebar").classList.toggle("active");
    });
});

// Khi người dùng nhấn vào overlay (bên ngoài thông báo), đóng thông báo
document.getElementById("logoutOverlay").onclick = function () {
    document.getElementById("logoutOverlay").style.display = "none";
    document.getElementById("logoutNotification").style.display = "none";
};
// Khi người dùng nhấn vào "Xóa"
document.querySelectorAll('.delete-button').forEach(button => {
    button.onclick = function () {
        // Lưu dòng sản phẩm cần xóa
        currentRowToDelete = this.closest('tr');

        // Hiển thị cả overlay và thông báo xóa sản phẩm
        document.getElementById("deleteOverlay").style.display = "block";
        document.getElementById("deleteNotification").style.display = "block";
    };
});

// Khi người dùng nhấn "Có" (Xác nhận xóa sản phẩm)
document.getElementById("confirmDeleteBtn").onclick = function () {
    // Xóa dòng sản phẩm
    currentRowToDelete.remove();

    // Ẩn cả overlay và thông báo xóa
    document.getElementById("deleteOverlay").style.display = "none";
    document.getElementById("deleteNotification").style.display = "none";
};

// Khi người dùng nhấn "Không" (Hủy xóa sản phẩm)
document.getElementById("cancelDeleteBtn").onclick = function () {
    // Ẩn cả overlay và thông báo xóa mà không làm gì
    document.getElementById("deleteOverlay").style.display = "none";
    document.getElementById("deleteNotification").style.display = "none";
};

// Khi người dùng nhấn vào overlay (bên ngoài thông báo), đóng thông báo xóa
document.getElementById("deleteOverlay").onclick = function () {
    document.getElementById("deleteOverlay").style.display = "none";
    document.getElementById("deleteNotification").style.display = "none";
};
// modal hóa đơn


