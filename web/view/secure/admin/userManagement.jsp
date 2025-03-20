<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
</head>
<body>
    <div class="container mt-4">
        <!-- Header và Sidebar -->
        <%@ include file="/view/secure/admin/header.jsp" %>
        <%@ include file="/view/secure/admin/sidebar.jsp" %>

        <div class="content">
            <div class="container mt-4">
                <div class="card p-4 shadow">
                    <h3>Quản lý người dùng <i class="fas fa-users"></i></h3>

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

                    <!-- Form tìm kiếm và bộ lọc -->
                    <form action="${pageContext.request.contextPath}/admin/userManagement" method="get" class="mb-3">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="searchKeyword">Tìm kiếm:</label>
                                <input type="text" class="form-control" name="searchKeyword" value="${searchKeyword}" placeholder="Tìm theo tên, email, số điện thoại">
                            </div>
                            <div class="col-md-3">
                                <label for="roleFilter">Lọc theo vai trò:</label>
                                <select name="roleFilter" id="roleFilter" class="form-control">
                                    <option value="">Tất cả</option>
                                    <option value="Owner" ${roleFilter == 'Owner' ? 'selected' : ''}>Owner</option>
                                    <option value="Police" ${roleFilter == 'Police' ? 'selected' : ''}>Police</option>
                                    <option value="Admin" ${roleFilter == 'Admin' ? 'selected' : ''}>Admin</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label for="lockedFilter">Lọc theo trạng thái:</label>
                                <select name="lockedFilter" id="lockedFilter" class="form-control">
                                    <option value="">Tất cả</option>
                                    <option value="locked" ${lockedFilter == 'locked' ? 'selected' : ''}>Đã khóa</option>
                                    <option value="unlocked" ${lockedFilter == 'unlocked' ? 'selected' : ''}>Chưa khóa</option>
                                </select>
                            </div>
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Nút mở modal thêm người dùng -->
                    <button type="button" class="btn btn-primary mb-3" data-toggle="modal" data-target="#addUserModal">
                        <i class="fas fa-plus"></i> Thêm người dùng
                    </button>

                    <!-- Bảng danh sách người dùng -->
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                                <tr>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=UserID&sortOrder=${sortBy == 'UserID' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            ID <i class="fas ${sortBy == 'UserID' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=FullName&sortOrder=${sortBy == 'FullName' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Họ tên <i class="fas ${sortBy == 'FullName' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=Email&sortOrder=${sortBy == 'Email' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Email <i class="fas ${sortBy == 'Email' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>
                                        <a href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=Phone&sortOrder=${sortBy == 'Phone' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}" class="text-white">
                                            Số điện thoại <i class="fas ${sortBy == 'Phone' ? (sortOrder == 'asc' ? 'fa-sort-up' : 'fa-sort-down') : 'fa-sort'}"></i>
                                        </a>
                                    </th>
                                    <th>Vai trò</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${users}">
                                    <tr>
                                        <td>${user.userID}</td>
                                        <td>${user.fullName}</td>
                                        <td>${user.email}</td>
                                        <td>${user.phone}</td>
                                        <td>${user.role}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.locked}">
                                                    <span class="badge badge-danger">Đã khóa</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-success">Chưa khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <!-- Nút sửa -->
                                            <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editUserModal${user.userID}">
                                                <i class="fas fa-edit"></i> Sửa
                                            </button>
                                            <!-- Nút khóa/mở khóa -->
                                            <form action="${pageContext.request.contextPath}/admin/userManagement" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="toggleLock">
                                                <input type="hidden" name="userId" value="${user.userID}">
                                                <input type="hidden" name="lock" value="${!user.locked}">
                                                <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                <input type="hidden" name="roleFilter" value="${roleFilter}">
                                                <input type="hidden" name="lockedFilter" value="${lockedFilter}">
                                                <input type="hidden" name="sortBy" value="${sortBy}">
                                                <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                <input type="hidden" name="page" value="${currentPage}">
                                                <button type="submit" class="btn btn-sm ${user.locked ? 'btn-success' : 'btn-danger'}">
                                                    <i class="fas ${user.locked ? 'fa-lock-open' : 'fa-lock'}"></i> ${user.locked ? 'Mở khóa' : 'Khóa'}
                                                </button>
                                            </form>
                                            <!-- Nút xóa -->
                                            <form action="${pageContext.request.contextPath}/admin/userManagement" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="userId" value="${user.userID}">
                                                <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                <input type="hidden" name="roleFilter" value="${roleFilter}">
                                                <input type="hidden" name="lockedFilter" value="${lockedFilter}">
                                                <input type="hidden" name="sortBy" value="${sortBy}">
                                                <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                <input type="hidden" name="page" value="${currentPage}">
                                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </form>
                                        </td>
                                    </tr>

                                    <!-- Modal sửa người dùng -->
                                    <div class="modal fade" id="editUserModal${user.userID}" tabindex="-1" role="dialog" aria-labelledby="editUserModalLabel${user.userID}" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="editUserModalLabel${user.userID}">Sửa người dùng</h5>
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">×</span>
                                                    </button>
                                                </div>
                                                <form action="${pageContext.request.contextPath}/admin/userManagement" method="post">
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="userId" value="${user.userID}">
                                                        <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                                                        <input type="hidden" name="roleFilter" value="${roleFilter}">
                                                        <input type="hidden" name="lockedFilter" value="${lockedFilter}">
                                                        <input type="hidden" name="sortBy" value="${sortBy}">
                                                        <input type="hidden" name="sortOrder" value="${sortOrder}">
                                                        <input type="hidden" name="page" value="${currentPage}">
                                                        <div class="form-group">
                                                            <label for="fullName">Họ tên:</label>
                                                            <input type="text" class="form-control" name="fullName" value="${user.fullName}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="email">Email:</label>
                                                            <input type="email" class="form-control" name="email" value="${user.email}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="phone">Số điện thoại:</label>
                                                            <input type="text" class="form-control" name="phone" value="${user.phone}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="role">Vai trò:</label>
                                                            <select name="role" class="form-control" required>
                                                                <option value="Owner" ${user.role == 'Owner' ? 'selected' : ''}>Owner</option>
                                                                <option value="Police" ${user.role == 'Police' ? 'selected' : ''}>Police</option>
                                                                <option value="Admin" ${user.role == 'Admin' ? 'selected' : ''}>Admin</option>
                                                            </select>
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
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage - 1}">Trước</a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/userManagement?searchKeyword=${searchKeyword}&roleFilter=${roleFilter}&lockedFilter=${lockedFilter}&sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage + 1}">Sau</a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Thông báo nếu không có người dùng -->
                    <c:if test="${empty users}">
                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle"></i> Không có người dùng nào phù hợp với tiêu chí.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal thêm người dùng -->
    <div class="modal fade" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addUserModalLabel">Thêm người dùng mới</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form action="${pageContext.request.contextPath}/admin/userManagement" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="searchKeyword" value="${searchKeyword}">
                        <input type="hidden" name="roleFilter" value="${roleFilter}">
                        <input type="hidden" name="lockedFilter" value="${lockedFilter}">
                        <input type="hidden" name="sortBy" value="${sortBy}">
                        <input type="hidden" name="sortOrder" value="${sortOrder}">
                        <input type="hidden" name="page" value="${currentPage}">
                        <div class="form-group">
                            <label for="fullName">Họ tên:</label>
                            <input type="text" class="form-control" name="fullName" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" class="form-control" name="email" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Mật khẩu:</label>
                            <input type="password" class="form-control" name="password" required>
                        </div>
                        <div class="form-group">
                            <label for="phone">Số điện thoại:</label>
                            <input type="text" class="form-control" name="phone" required>
                        </div>
                        <div class="form-group">
                            <label for="role">Vai trò:</label>
                            <select name="role" class="form-control" required>
                                <option value="Owner">Owner</option>
                                <option value="Police">Police</option>
                                <option value="Admin">Admin</option>
                            </select>
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

    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
</body>
</html>