<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ghi lỗi vi phạm - Police Dashboard</title>
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
                        <h3>Ghi lỗi vi phạm <i class="fas fa-exclamation-triangle"></i></h3>

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

                        <!-- Hiển thị thông tin xe -->
                        <c:if test="${not empty vehicle}">
                            <h4 class="mt-3">Thông tin xe</h4>
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

                            <!-- Form ghi lỗi vi phạm -->
                            <form action="${pageContext.request.contextPath}/police/recordViolation" method="post">
                                <input type="hidden" name="vehicleID" value="${vehicle.vehicleID}">
                                <input type="hidden" name="plateNumber" value="${vehicle.plateNumber}">

                                <div class="form-group">
                                    <label for="reason">Lý do vi phạm:</label>
                                    <textarea class="form-control" id="reason" name="reason" rows="3" required 
                                              placeholder="Nhập lý do vi phạm (VD: Không kiểm định khí thải đúng hạn)"></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="penaltyAmount">Số tiền phạt (VNĐ):</label>
                                    <input type="number" class="form-control" id="penaltyAmount" name="penaltyAmount" 
                                           step="0.01" required placeholder="Nhập số tiền phạt">
                                </div>

                                <button type="submit" class="btn btn-danger">
                                    <i class="fas fa-save"></i> Ghi lỗi vi phạm
                                </button>
                                <a href="${pageContext.request.contextPath}/police/inspectionLookup?plateNumber=${vehicle.plateNumber}" 
                                   class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Quay lại
                                </a>
                            </form>
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