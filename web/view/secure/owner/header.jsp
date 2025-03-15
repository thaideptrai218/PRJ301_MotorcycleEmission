<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark fixed-top" style="background-color: #1a2526;">
    <div class="container-fluid">
        <!-- Nút Toggle (Hamburger Menu) -->
        <button class="navbar-toggler me-3 " type="button" id="sidebarToggle" aria-label="Toggle navigation" style="display: block; margin-right: 10px">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Logo -->
        <a class="navbar-brand me-3" href="${pageContext.request.contextPath}/index.html">
            MotorcycleEmission
        </a>

        <!-- Spacer để đẩy các phần tử sang phải -->
        <div class="flex-grow-1"></div>

        <!-- Thông báo và Avatar -->
        <ul class="navbar-nav ms-auto">
            <!-- Thông báo (Dropdown) -->
            <li class="nav-item dropdown mx-2">
                <a class="nav-link dropdown-toggle" href="#" id="notificationsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="fas fa-bell"></i>
                    <span class="badge badge-danger">3</span>
                </a>
                <div class="dropdown-menu dropdown-menu-right dropdown-menu-lg" aria-labelledby="notificationsDropdown">
                    <a class="dropdown-item" href="#">Thông báo 1</a>
                    <a class="dropdown-item" href="#">Thông báo 2</a>
                    <a class="dropdown-item" href="#">Thông báo 3</a>
                </div>
            </li>

            <!-- Avatar -->
            <li class="nav-item dropdown mx-2">
                <a class="nav-link dropdown-toggle" href="#" id="avatarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <img src="${pageContext.request.contextPath}/assets/images/default-avatar.jpg" alt="Avatar" class="rounded-circle" width="30" height="30">
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="avatarDropdown">
                    <a class="dropdown-item" href="#">Hồ sơ</a>
                    <a class="dropdown-item" href="#">Cài đặt</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
