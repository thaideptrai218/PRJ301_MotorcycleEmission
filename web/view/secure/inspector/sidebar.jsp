<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="sidebar active" id="sidebar">
    <div class="user-info">
        <div class="user-info-avatar">
            <img src="${pageContext.request.contextPath}/assets/images/default-avatar.jpg" alt="Avatar" class="rounded-circle mb-2" width="50" height="50">
        </div>
        <div class="user-info-avatardesc">
            <h5>${sessionScope.userName}</h5>
            <p>${sessionScope.userEmail}</p>
            <p>Vai trò: ${sessionScope.role}</p>
            <p>SĐT: ${sessionScope.userPhone}</p>
        </div>
    </div>
    <hr class="bg-light">
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/inspector/home">
                <i class="fas fa-home"></i> Home
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/inspector/chooseWorkplace">
                <i class="fas fa-building"></i> Choose Workplace
            </a>
        </li>             
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/inspector/scheduleFormPage">
                <i class="fas fa-calendar-alt"></i> Check Schedules
            </a>
        </li>
    </ul>
    <div class="logout-section">
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-block">Đăng xuất</a>
    </div>
</div>

<style>
    .sidebar .nav-link {
        display: flex;
        align-items: center;
        padding: 10px 15px;
        color: white;
        transition: background-color 0.3s;
    }
    .sidebar .nav-link:hover {
        background-color: #ccc;
    }
    .sidebar .nav-link i {
        margin-right: 10px;
        width: 20px;
        text-align: center;
    }
    .logout-section {
        padding: 15px;
    }
    .logout-section .btn {
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .logout-section .btn i {
        margin-right: 8px;
    }
</style>