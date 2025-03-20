<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Schedules - Inspector Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    </head>
    <body>
        <div class="wrapper">
            <!-- Sidebar -->
            <%@ include file="/view/secure/inspector/sidebar.jsp" %>
            <%@ include file="/view/secure/inspector/header.jsp" %>

            <div class="content">
                <h2 class="mb-4">Danh Sách Lịch Kiểm Định</h2>

                <!-- Bộ lọc -->
                <form method="get" action="${pageContext.request.contextPath}/inspector/scheduleFormPage" class="filter-form mb-4">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label for="searchKeyword" class="form-label">Tìm kiếm theo biển số:</label>
                            <input type="text" name="searchKeyword" class="form-control" placeholder="Nhập biển số xe" value="${searchKeyword}">
                        </div>
                        <div class="col-md-3">
                            <label for="fromDate" class="form-label">Từ ngày:</label>
                            <input type="date" name="fromDate" class="form-control" value="${fromDate}">
                        </div>
                        <div class="col-md-3">
                            <label for="toDate" class="form-label">Đến ngày:</label>
                            <input type="date" name="toDate" class="form-control" value="${toDate}">
                        </div>
                        <div class="col-md-3">
                            <label for="statusFilter" class="form-label">Trạng thái:</label>
                            <select name="statusFilter" class="form-select">
                                <option value="">Tất cả</option>
                                <option value="Confirmed" ${statusFilter == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                <option value="Cancelled" ${statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                            </select>
                        </div>
                        <div class="col-md-12 d-flex justify-content-end">
                            <button type="submit" class="btn btn-primary">Lọc</button>
                        </div>
                    </div>
                </form>

                <!-- Bảng danh sách lịch kiểm định -->
                <c:choose>
                    <c:when test="${empty schedules}">
                        <div class="alert alert-warning" role="alert">
                            <i class="fas fa-info-circle"></i> Chưa có lịch kiểm định nào.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>
                                            <a href="?sortBy=ownerID&sortOrder=${sortBy == 'ownerID' && sortOrder == 'asc' ? 'desc' : 'asc'}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&statusFilter=${statusFilter}">
                                                Owner ID
                                                <c:if test="${sortBy == 'ownerID'}">
                                                    <c:if test="${sortOrder == 'asc'}"><i class="fas fa-arrow-up"></i></c:if>
                                                    <c:if test="${sortOrder == 'desc'}"><i class="fas fa-arrow-down"></i></c:if>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortBy=plateNumber&sortOrder=${sortBy == 'plateNumber' && sortOrder == 'asc' ? 'desc' : 'asc'}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&statusFilter=${statusFilter}">
                                                Biển số xe
                                                <c:if test="${sortBy == 'plateNumber'}">
                                                    <c:if test="${sortOrder == 'asc'}"><i class="fas fa-arrow-up"></i></c:if>
                                                    <c:if test="${sortOrder == 'desc'}"><i class="fas fa-arrow-down"></i></c:if>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortBy=createdAt&sortOrder=${sortBy == 'createdAt' && sortOrder == 'asc' ? 'desc' : 'asc'}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&statusFilter=${statusFilter}">
                                                Ngày tạo
                                                <c:if test="${sortBy == 'createdAt'}">
                                                    <c:if test="${sortOrder == 'asc'}"><i class="fas fa-arrow-up"></i></c:if>
                                                    <c:if test="${sortOrder == 'desc'}"><i class="fas fa-arrow-down"></i></c:if>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortBy=scheduleDate&sortOrder=${sortBy == 'scheduleDate' && sortOrder == 'asc' ? 'desc' : 'asc'}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&statusFilter=${statusFilter}">
                                                Ngày kiểm định
                                                <c:if test="${sortBy == 'scheduleDate'}">
                                                    <c:if test="${sortOrder == 'asc'}"><i class="fas fa-arrow-up"></i></c:if>
                                                    <c:if test="${sortOrder == 'desc'}"><i class="fas fa-arrow-down"></i></c:if>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortBy=status&sortOrder=${sortBy == 'status' && sortOrder == 'asc' ? 'desc' : 'asc'}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&statusFilter=${statusFilter}">
                                                Trạng thái
                                                <c:if test="${sortBy == 'status'}">
                                                    <c:if test="${sortOrder == 'asc'}"><i class="fas fa-arrow-up"></i></c:if>
                                                    <c:if test="${sortOrder == 'desc'}"><i class="fas fa-arrow-down"></i></c:if>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="schedule" items="${schedules}">
                                        <tr>
                                            <td>${schedule.ownerID}</td>
                                            <td>${schedule.plateNumber}</td>
                                            <td><fmt:formatDate value="${schedule.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                                            <td><fmt:formatDate value="${schedule.scheduleDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                            <td>${schedule.status}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${schedule.status != 'Completed'}">
                                                        <a href="${pageContext.request.contextPath}/inspector/inspectionSchedule?vehicleID=${schedule.vehicleID}&stationID=${schedule.stationID}&scheduleID=${schedule.scheduleID}" 
                                                           class="btn btn-sm btn-primary btn-action" title="Kiểm định">
                                                            <i class="fas fa-tools"></i> Kiểm định
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/inspector/inspectionSchedule?vehicleID=${schedule.vehicleID}&stationID=${schedule.stationID}&scheduleID=${schedule.scheduleID}&reinspect=true" 
                                                           class="btn btn-sm btn-warning btn-action" title="Tái kiểm định">
                                                            <i class="fas fa-redo"></i> Tái kiểm định
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
    </body>
</html>