<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tra cứu kiểm định khí thải - Police Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <!-- Header và Sidebar -->
            <%@ include file="/view/secure/police/header.jsp" %>
            <%@ include file="/view/secure/police/sidebar.jsp" %>

            <div class="content">
                <div class="container mt-4">
                    <div class="card p-4 shadow">
                        <h3>Tra cứu kiểm định khí thải <i class="fas fa-search"></i></h3>

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

                        <!-- Form tra cứu -->
                        <form action="${pageContext.request.contextPath}/police/inspectionLookup" method="post">
                            <div class="form-group">
                                <label for="plateNumber">Biển số xe:</label>
                                <input type="text" class="form-control" id="plateNumber" name="plateNumber" 
                                       placeholder="Nhập biển số xe (VD: 51H-12345)" value="${not empty plateNumber ? plateNumber : ""}" required>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i> Tra cứu
                            </button>
                        </form>

                        <!-- Hiển thị thông tin xe và lịch sử kiểm định -->
                        <c:if test="${not empty vehicle}">
                            <h4 class="mt-4">Thông tin xe</h4>
                            <div class="card p-3 mb-3">
                                <p><strong>Biển số:</strong> ${vehicle.plateNumber}</p>
                                <p><strong>Chủ sở hữu:</strong> 
                                    <c:choose>
                                        <c:when test="${not empty owner}">
                                            ${owner.fullName}
                                        </c:when>
                                        <c:otherwise>
                                            Không tìm thấy thông tin
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <p><strong>Số điện thoại:</strong> 
                                    <c:choose>
                                        <c:when test="${not empty owner}">
                                            ${owner.phone}
                                        </c:when>
                                        <c:otherwise>
                                            Không có thông tin
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <p><strong>Hãng xe:</strong> ${vehicle.brand}</p>
                                <p><strong>Mẫu xe:</strong> ${vehicle.model}</p>
                                <p><strong>Năm sản xuất:</strong> ${vehicle.manufactureYear}</p>
                                <p><strong>Số máy:</strong> ${vehicle.engineNumber}</p>
                            </div>

                            <!-- Nút ghi lỗi vi phạm -->
                            <a href="${pageContext.request.contextPath}/police/recordViolation?plateNumber=${vehicle.plateNumber}" 
                               class="btn btn-danger mb-3">
                                <i class="fas fa-exclamation-triangle"></i> Ghi lỗi vi phạm
                            </a>

                            <h4>Lịch sử kiểm định khí thải</h4>
                            <c:choose>
                                <c:when test="${empty inspectionRecords}">
                                    <div class="alert alert-warning" role="alert">
                                        <i class="fas fa-info-circle"></i> Xe chưa có lịch sử kiểm định khí thải.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Ngày kiểm định</th>
                                                    <th>Trạm kiểm định</th>
                                                    <th>Kết quả</th>
                                                    <th>CO₂ Emission (g/km)</th>
                                                    <th>HC Emission (g/km)</th>
                                                    <th>Ngày hết hạn</th>
                                                    <th>Ghi chú</th>
                                                    <th>Trạng thái</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="record" items="${inspectionRecords}">
                                                    <tr>
                                                        <td><fmt:formatDate value="${record.inspectionDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                                        <td>${record.stationName}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${record.result == 'Pass'}">
                                                                    <span class="badge badge-success">Pass</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge badge-danger">Decline</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>${record.co2Emission}</td>
                                                        <td>${record.hcEmission}</td>
                                                        <td><fmt:formatDate value="${record.expirationDate}" pattern="dd/MM/yyyy" /></td>
                                                        <td>${record.comments}</td>
                                                        <td>${record.status}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <!-- Hiển thị lịch sử vi phạm -->
                            <h4 class="mt-4">Lịch sử vi phạm</h4>
                            <c:choose>
                                <c:when test="${empty violations}">
                                    <div class="alert alert-info" role="alert">
                                        <i class="fas fa-info-circle"></i> Xe chưa có lịch sử vi phạm.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
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
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- JavaScript -->
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
        </div>
    </body>
</html>