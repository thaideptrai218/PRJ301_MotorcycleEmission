<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Vehicle Inspection - Inspector Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <%@ include file="/view/secure/inspector/header.jsp" %>
            <%@ include file="/view/secure/inspector/sidebar.jsp" %>

            <div class="content">
                <div class="container mt-4">
                    <div class="card p-4 shadow">
                        <h3>Vehicle Inspection <i class="fas fa-car"></i></h3>

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

                        <c:if test="${not empty vehicle}">
                            <!-- FORM gửi dữ liệu kiểm định -->
                            <form action="${pageContext.request.contextPath}/inspector/inspectionSchedule" method="post">
                                <!-- Trường ẩn cho vehicleID và stationID -->
                                <input type="hidden" name="vehicleID" value="${vehicle.vehicleID}">
                                <input type="hidden" name="stationID" value="${stationID}">

                                <!-- Hiển thị thông tin xe (readonly) -->
                                <h4 class="mt-3">Vehicle Details</h4>
                                <div class="form-group">
                                    <label>Vehicle ID:</label>
                                    <input type="text" class="form-control" value="${vehicle.vehicleID}" readonly>
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

                                <!-- Nhập thông tin kiểm định -->
                                <h4 class="mt-4">Inspection Details</h4>
                                <div class="form-group">
                                    <label>CO₂ Emission (g/km):</label>
                                    <input type="number" class="form-control" name="co2Emission" step="0.01" value="0.0" required>
                                </div>
                                <div class="form-group">
                                    <label>HC Emission (g/km):</label>
                                    <input type="number" class="form-control" name="hcEmission" step="0.01" value="0.0" required>
                                </div>
                                <div class="form-group">
                                    <label>Expiration Date:</label>
                                    <input type="date" class="form-control" name="expirationDate" required>
                                </div>
                                <div class="form-group">
                                    <label>Comments:</label>
                                    <textarea class="form-control" name="comments" rows="3"></textarea>
                                </div>

                                <!-- Chọn kết quả kiểm định -->
                                <h4 class="mt-4">Inspection Result</h4>
                                <div class="form-group">
                                    <label>Result:</label>
                                    <select class="form-control" name="result" required>
                                        <option value="">-- Select Result --</option>
                                        <option value="Pass">Pass</option>
                                        <option value="Decline">Decline</option>
                                    </select>
                                </div>

                                <!-- Nút Submit -->
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-check"></i> Submit Inspection
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- JavaScript -->
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
        </div>
    </body>
</html>