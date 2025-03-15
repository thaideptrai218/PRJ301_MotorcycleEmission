<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notifications - Owner Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        .pagination {
            margin-top: 20px;
        }
        .action-btn {
            margin-right: 5px;
        }
        .status-icon {
            cursor: pointer;
            font-size: 1.2em;
        }
        .status-tick {
            color: green;
        }
        .status-eye {
            color: gray;
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <%@ include file="/view/secure/owner/header.jsp" %>
        <%@ include file="/view/secure/owner/sidebar.jsp" %>

        <!-- Main Content -->
        <div class="content">
            <div class="container mt-4">
                <h2>Notifications</h2>
                <div class="" id="notifications">
                    <h3>My Notifications <i class="fas fa-bell"></i></h3>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger status-error" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                        <c:set var="errorMessageDisplayed" value="true" scope="request" />
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success status-success" role="alert">
                            <i class="fas fa-check-circle"></i> ${successMessage}
                        </div>
                        <c:set var="successMessageDisplayed" value="true" scope="request" />
                    </c:if>

                    <c:if test="${not empty notifications and not empty notifications}">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Message</th>
                                    <th>Type</th>
                                    <th>Sent Date</th>
                                    <th>Seen</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="notification" items="${notifications}">
                                    <tr class="${!notification.isRead ? 'font-weight-bold' : ''}">
                                        <td>${notification.message}</td>
                                        <td>${notification.notificationType}</td>
                                        <td>
                                            <fmt:formatDate value="${notification.sentDate}" pattern="dd/MM/yyyy HH:mm:ss" />
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${!notification.isRead}">
                                                    <form action="${pageContext.request.contextPath}/owner/notificationsPage" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="markAsRead">
                                                        <input type="hidden" name="notificationId" value="${notification.notificationID}">
                                                        <input type="hidden" name="page" value="${currentPage}">
                                                        <a href="#" onclick="$(this).closest('form').submit(); return false;" class="status-icon status-eye">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-icon status-tick">
                                                        <i class="fas fa-check"></i>
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/owner/notificationsPage" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="notificationId" value="${notification.notificationID}">
                                                <input type="hidden" name="page" value="${currentPage}">
                                                <button type="submit" class="btn btn-link action-btn" onclick="return confirm('Bạn có chắc chắn muốn xóa thông báo này?');">
                                                    <i class="fas fa-trash-alt text-danger"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Phân trang -->
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="${pageContext.request.contextPath}/owner/notificationsPage?page=${currentPage - 1}" aria-label="Previous">
                                            <span aria-hidden="true">«</span>
                                        </a>
                                    </li>
                                </c:if>
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/owner/notificationsPage?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="${pageContext.request.contextPath}/owner/notificationsPage?page=${currentPage + 1}" aria-label="Next">
                                            <span aria-hidden="true">»</span>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </c:if>
                    <c:if test="${empty notifications or empty notifications}">
                        <p class="status-pending"><i class="fas fa-info-circle"></i> Bạn chưa có thông báo nào.</p>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- JavaScript -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            $(document).ready(function () {
                $('#sidebarToggle').on('click', function () {
                    $('#sidebar').toggleClass('active');
                    if ($('#sidebar').hasClass('active')) {
                        $('.sidebar').css('display', 'block');
                    } else {
                        $('.sidebar').css('display', 'none');
                    }
                });
            });
        </script>
    </div>
</body>
</html>