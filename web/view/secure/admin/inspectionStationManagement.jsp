<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <title>Quản lý trạm kiểm định - Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .content {
            margin-left: 250px;
            padding: 20px;
        }
        .card {
            border-radius: 10px;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .stats-section {
            margin-top: 30px;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .stats-section h5 {
            margin-bottom: 20px;
        }
        .stats-bar {
            height: 20px;
            border-radius: 5px;
            margin-bottom: 10px;
        }
        .stats-bar.pass {
            background-color: #28a745;
        }
        .stats-bar.fail {
            background-color: #dc3545;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <!-- Header và Sidebar -->
        <%@ include file="/view/secure/admin/header.jsp" %>
        <%@ include file="/view/secure/admin/sidebar.jsp" %>

        <div class="content">
            <div class="container mt-4">
                <div class="card p-4 shadow">
                    <h3>Quản lý trạm kiểm định <i class="fas fa-building"></i></h3>

                    <!-- Hiển thị thông báo lỗi hoặc thành công -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle"></i> ${successMessage}
                        </div>
                    </c:if>

                    <!-- Form tìm kiếm -->
                    <form action="${pageContext.request.contextPath}/admin/inspectionStationManagement" method="get" class="mb-3">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="searchKeyword">Tìm kiếm:</label>
                                <input type="text" class="form-control" name="searchKeyword" value="${searchKeyword}" placeholder="Tìm theo tên, địa chỉ, số điện thoại, email">
                            </div>
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Nút mở modal thêm trạm -->
                    <button type="button" class="btn btn-primary mb-3" data-toggle="modal" data-target="#addStationModal">
                        <i class="fas fa-plus"></i> Thêm trạm kiểm định
                    </button>

                    <!-- Bảng danh sách trạm kiểm định -->
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=StationID&sortOrder=${sortBy == 'StationID' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            ID <i class="fas ${sortBy == 'StationID' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=Name&sortOrder=${sortBy == 'Name' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Tên trạm <i class="fas ${sortBy == 'Name' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=Address&sortOrder=${sortBy == 'Address' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Địa chỉ <i class="fas ${sortBy == 'Address' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=Phone&sortOrder=${sortBy == 'Phone' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Số điện thoại <i class="fas ${sortBy == 'Phone' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=Email&sortOrder=${sortBy == 'Email' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Email <i class="fas ${sortBy == 'Email' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=InspectionCount&sortOrder=${sortBy == 'InspectionCount' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Số kiểm định <i class="fas ${sortBy == 'InspectionCount' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="station" items="${stations}">
                                    <tr>
                                        <td>${station.stationID}</td>
                                        <td>${station.name}</td>
                                        <td>${station.address}</td>
                                        <td>${station.phone}</td>
                                        <td>${station.email}</td>
                                        <td>${station.inspectionCount}</td>
                                        <td>
                                            <!-- Nút sửa -->
                                            <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editStationModal${station.stationID}">
                                                <i class="fas fa-edit"></i> Sửa
                                            </button>
                                            <!-- Form xóa trạm kiểm định -->
                                            <form action="${pageContext.request.contextPath}/admin/inspectionStationManagement" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="stationId" value="${station.stationID}">
                                                <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                <input type="hidden" name="sortBy" value="${sortBy}">
                                                <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                <input type="hidden" name="page" value="${currentPage}">
                                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa trạm kiểm định này?');">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </form>
                                            <!-- Form xem thống kê -->
                                            <form action="${pageContext.request.contextPath}/admin/inspectionStationManagement" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="viewStats">
                                                <input type="hidden" name="stationId" value="${station.stationID}">
                                                <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                <input type="hidden" name="sortBy" value="${sortBy}">
                                                <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                <input type="hidden" name="page" value="${currentPage}">
                                                <button type="submit" class="btn btn-info btn-sm">
                                                    <i class="fas fa-chart-pie"></i> Thống kê
                                                </button>
                                            </form>
                                        </td>
                                    </tr>

                                    <!-- Modal sửa trạm kiểm định -->
                                    <div class="modal fade" id="editStationModal${station.stationID}" tabindex="-1" role="dialog" aria-labelledby="editStationModalLabel${station.stationID}" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="editStationModalLabel${station.stationID}">Sửa trạm kiểm định</h5>
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <form action="${pageContext.request.contextPath}/admin/inspectionStationManagement" method="post">
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="stationId" value="${station.stationID}">
                                                        <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                        <input type="hidden" name="sortBy" value="${sortBy}">
                                                        <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                        <input type="hidden" name="page" value="${currentPage}">
                                                        <div class="form-group">
                                                            <label for="name">Tên trạm:</label>
                                                            <input type="text" class="form-control" name="name" value="${station.name}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="address">Địa chỉ:</label>
                                                            <textarea class="form-control" name="address" required>${station.address}</textarea>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="phone">Số điện thoại:</label>
                                                            <input type="text" class="form-control" name="phone" value="${station.phone}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="email">Email:</label>
                                                            <input type="email" class="form-control" name="email" value="${station.email}" required>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                                                        <button type="submit" class="btn btn-primary">Lưu</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Phân trang -->
                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage - 1}">Trước</a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/inspectionStationManagement?searchKeyword=${searchKeyword}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage + 1}">Sau</a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Thông báo nếu không có trạm -->
                    <c:if test="${empty stations}">
                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle"></i> Không có trạm kiểm định nào phù hợp với tiêu chí.
                        </div>
                    </c:if>

                    <!-- Hiển thị thống kê nếu có -->
                    <c:if test="${not empty stationStats and not empty station}">
                        <div class="stats-section">
                            <h5>Thống kê hoạt động trạm: ${station.name}</h5>
                            <p><strong>Tổng số kiểm định:</strong> ${stationStats.totalInspections}</p>
                            <p><strong>Số xe đạt:</strong> ${stationStats.passCount}</p>
                            <p><strong>Số xe không đạt:</strong> ${stationStats.failCount}</p>
                            <div>
                                <strong>Tỷ lệ đạt:</strong> ${stationStats.passRate}% 
                                <div class="stats-bar pass" style="width: ${stationStats.passRate}%"></div>
                            </div>
                            <div>
                                <strong>Tỷ lệ không đạt:</strong> ${stationStats.failRate}% 
                                <div class="stats-bar fail" style="width: ${stationStats.failRate}%"></div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal thêm trạm kiểm định -->
    <div class="modal fade" id="addStationModal" tabindex="-1" role="dialog" aria-labelledby="addStationModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addStationModalLabel">Thêm trạm kiểm định mới</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/admin/inspectionStationManagement" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                        <input type="hidden" name="sortBy" value="${sortBy}">
                        <input type="hidden" name="sortOrder" value="${sortOrder}">
                        <input type="hidden" name="page" value="${currentPage}">
                        <div class="form-group">
                            <label for="name">Tên trạm:</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="address">Địa chỉ:</label>
                            <textarea class="form-control" name="address" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="phone">Số điện thoại:</label>
                            <input type="text" class="form-control" name="phone" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" class="form-control" name="email" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Thêm</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- JavaScript để mở/đóng modal -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>