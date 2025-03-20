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
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/home">
                <i class="fas fa-home"></i> Home
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/addVehiclePage">
                <i class="fas fa-car"></i> Add Vehicle
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/vehiclesPage">
                <i class="fas fa-list"></i> Vehicles
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/scheduleInspectionPage">
                <i class="fas fa-calendar-alt"></i> Schedule Inspection
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/inspectionRequests">
                <i class="fas fa-file-alt"></i> Inspection Requests
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/trackHistoryPage">
                <i class="fas fa-history"></i> Track History
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/notificationsPage">
                <i class="fas fa-bell"></i> Notifications
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/violationLookup">
                <i class="fas fa-exclamation-triangle"></i> Tra cứu vi phạm
            </a>
        </li>
    </ul>
    <div class="logout-section">
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-block">
            <i class="fas fa-sign-out-alt"></i> Đăng xuất
        </a>
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