<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Verify Vehicle Requests</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Xác Minh Yêu Cầu Xe</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
            <c:remove var="successMessage" scope="session" />
        </c:if>

        <!-- Bộ lọc -->
        <form method="get" action="${pageContext.request.contextPath}/station/verifyVehicle" class="mb-3">
            <div class="form-row">
                <div class="form-group col-md-3">
                    <label for="statusFilter">Trạng thái:</label>
                    <select name="statusFilter" class="form-control" onchange="this.form.submit()">
                        <option value="">Tất cả</option>
                        <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                        <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
                    </select>
                </div>
                <div class="form-group col-md-3">
                    <label for="searchKeyword">Tìm kiếm:</label>
                    <input type="text" name="searchKeyword" class="form-control" placeholder="Tìm kiếm theo nội dung..." value="${searchKeyword}">
                </div>
                <div class="form-group col-md-2">
                    <label for="fromDate">Từ ngày:</label>
                    <input type="date" name="fromDate" class="form-control" value="${fromDate}">
                </div>
                <div class="form-group col-md-2">
                    <label for="toDate">Đến ngày:</label>
                    <input type="date" name="toDate" class="form-control" value="${toDate}">
                </div>
                <div class="form-group col-md-2 align-self-end">
                    <button type="submit" class="btn btn-primary">Lọc</button>
                </div>
            </div>
        </form>

        <!-- Bảng danh sách yêu cầu -->
        <c:choose>
            <c:when test="${empty requests}">
                <p class="text-muted">Không có yêu cầu xác minh xe nào.</p>
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Created By</th>
                            <th>
                                <a href="?sortBy=createDate&sortOrder=${sortBy == 'createDate' && sortOrder == 'asc' ? 'desc' : 'asc'}">
                                    Create Date
                                    <c:if test="${sortBy == 'createDate'}">
                                        <c:if test="${sortOrder == 'asc'}">
                                            <i class="fas fa-arrow-up"></i>
                                        </c:if>
                                        <c:if test="${sortOrder == 'desc'}">
                                            <i class="fas fa-arrow-down"></i>
                                        </c:if>
                                    </c:if>
                                </a>
                            </th>
                            <th>Message</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${requests}">
                            <tr>
                                <td>${request.createdBy}</td>
                                <td><fmt:formatDate value="${request.createDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td>${request.message}</td>
                                <td>
                                    <c:if test="${request.status == 'Pending'}">
                                        <form method="post" action="${pageContext.request.contextPath}/station/verifyVehicle" style="display:inline;">
                                            <input type="hidden" name="requestId" value="${request.requestID}">
                                            <input type="hidden" name="action" value="approve">
                                            <button type="submit" class="btn btn-link p-0" title="Approve">
                                                <i class="fas fa-check-circle text-success"></i>
                                            </button>
                                        </form>
                                         
                                        <form method="post" action="${pageContext.request.contextPath}/station/verifyVehicle" style="display:inline;">
                                            <input type="hidden" name="requestId" value="${request.requestID}">
                                            <input type="hidden" name="action" value="reject">
                                            <button type="submit" class="btn btn-link p-0" title="Reject">
                                                <i class="fas fa-times-circle text-danger"></i>
                                            </button>
                                        </form>
                                         
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/station/verifyVehicle?requestId=${request.requestID}&page=${currentPage}&statusFilter=${statusFilter}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&sortBy=${sortBy}&sortOrder=${sortOrder}" title="View Details">
                                        <i class="fas fa-eye text-primary"></i>
                                    </a>
                                    <c:if test="${request.status != 'Pending'}">
                                         
                                        <span class="badge badge-secondary">${request.status}</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Phân trang -->
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <c:if test="${currentPage > 1}">
                            <li class="page-item"><a class="page-link" href="?page=${currentPage - 1}&statusFilter=${statusFilter}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&sortBy=${sortBy}&sortOrder=${sortOrder}">Previous</a></li>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&statusFilter=${statusFilter}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&sortBy=${sortBy}&sortOrder=${sortOrder}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <li class="page-item"><a class="page-link" href="?page=${currentPage + 1}&statusFilter=${statusFilter}&searchKeyword=${searchKeyword}&fromDate=${fromDate}&toDate=${toDate}&sortBy=${sortBy}&sortOrder=${sortOrder}">Next</a></li>
                        </c:if>
                    </ul>
                </nav>
            </c:otherwise>
        </c:choose>

        <!-- Form chi tiết yêu cầu -->
        <c:if test="${not empty selectedRequest && not empty user && not empty vehicle}">
            <div class="mt-4">
                <h4>Chi Tiết Yêu Cầu</h4>

                <h5>Thông Tin Người Dùng</h5>
                <div class="form-group">
                    <label>User ID:</label>
                    <input type="text" class="form-control" value="${user.userID}" readonly>
                </div>
                <div class="form-group">
                    <label>Full Name:</label>
                    <input type="text" class="form-control" value="${user.fullName}" readonly>
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="text" class="form-control" value="${user.email}" readonly>
                </div>
                <div class="form-group">
                    <label>Phone:</label>
                    <input type="text" class="form-control" value="${user.phone}" readonly>
                </div>
                <div class="form-group">
                    <label>Role:</label>
                    <input type="text" class="form-control" value="${user.role}" readonly>
                </div>

                <h5>Thông Tin Xe</h5>
                <div class="form-group">
                    <label>Owner ID:</label>
                    <input type="text" class="form-control" value="${vehicle.ownerID}" readonly>
                </div>
                <div class="form-group">
                    <label>Plate Number:</label>
                    <input type="text" class="form-control" value="${vehicle.plateNumber}" readonly>
                </div>
                <div class="form-group">
                    <label>Brand:</label>
                    <input type="text" class="form-control" value="${vehicle.brand}" readonly>
                </div>
                <div class="form-group">
                    <label>Model:</label>
                    <input type="text" class="form-control" value="${vehicle.model}" readonly>
                </div>
                <div class="form-group">
                    <label>Manufacture Year:</label>
                    <input type="text" class="form-control" value="${vehicle.manufactureYear}" readonly>
                </div>
                <div class="form-group">
                    <label>Engine Number:</label>
                    <input type="text" class="form-control" value="${vehicle.engineNumber}" readonly>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>