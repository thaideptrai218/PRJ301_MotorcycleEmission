<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Sidebar -->
<div class="sidebar bg-dark text-white" id="sidebar">
    <!-- User Info -->
    <div class="user-info p-3 text-center">
        <img src="${pageContext.request.contextPath}/assets/images/default-avatar.png" alt="Avatar" class="rounded-circle mb-2" width="80" height="80">
        <h5>${sessionScope.username}</h5>
        <p>${sessionScope.email}</p>
        <p>Vai trò: ${sessionScope.role}</p>
        <p>SĐT: ${sessionScope.phone}</p>
    </div>
    <hr class="bg-light">

    <!-- Navigation Links -->
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-home"></i> Home
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/news">
                <i class="fas fa-newspaper"></i> News
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/vehicle">
                <i class="fas fa-car"></i> Vehicle
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/request">
                <i class="fas fa-file-alt"></i> Request
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/history">
                <i class="fas fa-history"></i> History
            </a>
        </li>
    </ul>

    <!-- Logout Button -->
    <div class="logout-section position-absolute w-100 bottom-0 p-3">
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-block">Đăng xuất</a>
    </div>
</div>

<!-- CSS cho Sidebar -->
<style>
    .sidebar {
        position: fixed;
        top: 56px; /* Điều chỉnh nếu navbar có chiều cao khác */
        left: 0;
        width: 300px; /* Tăng độ rộng lên 300px */
        height: calc(100vh - 56px); /* Chiều cao từ dưới navbar đến cuối trang */
        overflow-y: auto;
        z-index: 1000;
        transition: transform 0.3s ease; /* Hiệu ứng mượt mà khi ẩn/hiện */
    }
    .sidebar.hidden {
        transform: translateX(-100%); /* Ẩn hoàn toàn sidebar */
    }
    .nav-link {
        font-size: 1.1rem;
        margin: 0 15px; /* Thêm margin trái phải cho các nút */
        transition: background-color 0.3s ease;
    }
    .nav-link:hover {
        background-color: #ff5722; /* Hiệu ứng hover */
    }
    .nav-link i {
        margin-right: 10px;
    }
    .logout-section {
        background-color: inherit; /* Kế thừa màu nền sidebar */
    }
</style>

<!-- JavaScript cho nút toggle -->
<script>
    document.getElementById("sidebarToggle").addEventListener("click", function () {
        document.getElementById("sidebar").classList.toggle("hidden");
    });
</script>