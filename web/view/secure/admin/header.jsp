<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark fixed-top" style="background-color: #1a2526;">
    <div class="container-fluid">
        <!-- Nút Toggle (Hamburger Menu) -->
        <button class="navbar-toggler me-3" type="button" id="sidebarToggle" aria-label="Toggle navigation" style="display: block; margin-right: 10px">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Logo -->
        <a class="navbar-brand me-3" href="${pageContext.request.contextPath}/index.html">
            Motorcycle Emission
        </a>

        <!-- Spacer để đẩy các phần tử sang phải -->
        <div class="flex-grow-1"></div>

        <!-- Thông báo và Avatar -->
        <ul class="navbar-nav ms-auto">
            <!-- Thông báo (Dropdown) -->
            <li class="nav-item dropdown mx-2">
                <a class="nav-link dropdown-toggle" href="#" id="notificationsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="fas fa-bell"></i>
                    <c:if test="${not empty sessionScope.unreadCount and sessionScope.unreadCount > 0}">
                        <span class="badge badge-danger">${sessionScope.unreadCount}</span>
                    </c:if>
                </a>
                <div class="dropdown-menu dropdown-menu-right dropdown-menu-lg" aria-labelledby="notificationsDropdown" style="max-height: 300px; overflow-y: auto;">
                    <c:choose>
                        <c:when test="${empty sessionScope.topUnreadNotifications}">
                            <div class="dropdown-item text-center text-muted">No unread notifications</div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${sessionScope.topUnreadNotifications}">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/owner/notificationsPage">
                                    <div style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                        <span>${notification.message}</span>
                                        <div style="font-size: 0.8em; color: #6c757d;">
                                            <fmt:formatDate value="${notification.sentDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item text-center" href="${pageContext.request.contextPath}/owner/notificationsPage">Show All Notifications</a>
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

<!-- JavaScript -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>