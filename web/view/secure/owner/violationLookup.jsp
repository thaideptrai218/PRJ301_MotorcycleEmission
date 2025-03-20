<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tra cứu vi phạm - Owner Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap4.min.css">
</head>
<body>
    <div class="container mt-4">
        <!-- Header và Sidebar (giả định bạn đã có) -->
        <%@ include file="/view/secure/owner/header.jsp" %>
        <%@ include file="/view/secure/owner/sidebar.jsp" %>

        <div class="content">
            <div class="container mt-4">
                <div class="card p-4 shadow">
                    <h3>Tra cứu vi phạm <i class="fas fa-exclamation-triangle"></i></h3>

                    <!-- Hiển thị thông báo lỗi hoặc thành công -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </c:if>

                    <!-- Form bộ lọc -->
                    <form action="${pageContext.request.contextPath}/owner/violationLookup" method="get" class="mb-3">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="statusFilter">Lọc theo trạng thái:</label>
                                <select name="statusFilter" id="statusFilter" class="form-control">
                                    <option value="">Tất cả</option>
                                    <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                                    <option value="Resolved" ${statusFilter == 'Resolved' ? 'selected' : ''}>Resolved</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label for="sortOrder">Sắp xếp theo ngày:</label>
                                <select name="sortOrder" id="sortOrder" class="form-control">
                                    <option value="desc" ${sortOrder == 'desc' || sortOrder == null ? 'selected' : ''}>Mới nhất trước</option>
                                    <option value="asc" ${sortOrder == 'asc' ? 'selected' : ''}>Cũ nhất trước</option>
                                </select>
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-filter"></i> Lọc
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Bảng danh sách vi phạm -->
                    <div class="table-responsive">
                        <table id="violationTable" class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>Biển số xe</th>
                                    <th>Ngày vi phạm</th>
                                    <th>Lý do</th>
                                    <th>Số tiền phạt (VNĐ)</th>
                                    <th>Trạng thái</th>
                                    <th>Cảnh sát xử lý</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="violation" items="${violations}">
                                    <tr>
                                        <td>${violation.plateNumber}</td>
                                        <td><fmt:formatDate value="${violation.violationDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                        <td>${violation.reason}</td>
                                        <td>${violation.penaltyAmount}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${violation.status == 'Pending'}">
                                                    <span class="badge badge-warning">Pending</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-success">Resolved</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${violation.policeName}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Thông báo nếu không có vi phạm -->
                    <c:if test="${empty violations}">
                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle"></i> Bạn chưa có vi phạm nào.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap4.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#violationTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Vietnamese.json"
                },
                "pageLength": 10,
                "order": [] // Không áp dụng sắp xếp mặc định, để server-side xử lý
            });
        });
    </script>
</body>
</html>