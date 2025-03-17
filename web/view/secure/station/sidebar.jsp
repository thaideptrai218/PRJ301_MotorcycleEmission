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
            <a class="nav-link" href="${pageContext.request.contextPath}/station/home">Home</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/station/requests">Add Vehicle</a>
        </li><!--
-->        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/station/chooseWorkplace">Choose Workplace</a>
        </li><!--
-->        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/station/verifyVehicle">Verify Vehicle</a>
        </li><!--
-->        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/station/inspectionSchedule">Inspection Requests</a>
        </li><!--
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/trackHistoryPage">Track History</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/owner/notificationsPage">Notifications</a>
        </li>-->
    </ul>
    <div class="logout-section">
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-block">Đăng xuất</a>
    </div>
</div>