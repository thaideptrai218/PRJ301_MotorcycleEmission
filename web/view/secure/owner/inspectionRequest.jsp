<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inspection Requests</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .status-pending { color: #ffc107; font-weight: bold; } /* Vàng */
        .status-confirmed { color: #28a745; font-weight: bold; } /* Xanh lá */
        .status-cancelled { color: #dc3545; font-weight: bold; } /* Đỏ */
        .status-completed { color: #17a2b8; font-weight: bold; } /* Xanh dương */
        .clickable { cursor: pointer; text-decoration: underline; }
    </style>
</head>
<body>
    <div class="wrapper">
        <%@ include file="/view/secure/owner/header.jsp" %>
        <%@ include file="/view/secure/owner/sidebar.jsp" %>

        <div class="content">
            <div class="container mt-4" style="margin-left: 200px;">
                <h2>Inspection Requests</h2>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <c:choose>
                    <c:when test="${empty inspectionSchedules}">
                        <p class="text-muted">Bạn chưa có yêu cầu kiểm định nào.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Plate Number</th>
                                    <th>Station Name</th>
                                    <th>Schedule Date</th>
                                    <th>Status</th>
                                    <th>Created At</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="schedule" items="${inspectionSchedules}">
                                    <tr>
                                        <td>${schedule.plateNumber}</td>
                                        <td>${schedule.stationName}</td>
                                        <td><fmt:formatDate value="${schedule.scheduleDate}" pattern="dd/MM/yyyy" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${schedule.status != 'Pending'}">
                                                    <a href="${pageContext.request.contextPath}/owner/inspectionRecords?scheduleId=${schedule.scheduleID}" 
                                                       class="status-${schedule.status.toLowerCase()} clickable">
                                                        ${schedule.status}
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-${schedule.status.toLowerCase()}">${schedule.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><fmt:formatDate value="${schedule.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>