<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Owner Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/registerAndLogin.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
</head>
<body>
    <%@ include file="/view/header.jsp" %>

    <div class="container mt-4">
        <h2>Owner Dashboard</h2>

        <!-- Tab Navigation -->
        <ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item">
                <a class="nav-link" id="add-vehicle-tab" data-toggle="tab" href="#add-vehicle">Add Vehicle</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" id="schedule-inspection-tab" data-toggle="tab" href="#schedule-inspection">Schedule Inspection</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="track-history-tab" data-toggle="tab" href="#track-history">Track History</a>
            </li>
        </ul>

        <!-- Tab Content -->
        <div class="tab-content mt-3" id="myTabContent">

            <!-- Tab: Add Vehicle -->
            <div class="tab-pane fade" id="add-vehicle" role="tabpanel">
                <h3>Add New Vehicle</h3>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </c:if>
                <form action="${pageContext.request.contextPath}/addVehicle" method="POST">
                    <input type="hidden" name="ownerID" value="${sessionScope.userId}">
                    <div class="form-group">
                        <label for="plateNumber">Plate Number <span style="color:red;">*</span></label>
                        <input type="text" class="form-control" id="plateNumber" name="plateNumber" maxlength="15" required>
                    </div>
                    <div class="form-group">
                        <label for="brand">Brand <span style="color:red;">*</span></label>
                        <input type="text" class="form-control" id="brand" name="brand" maxlength="50" required>
                    </div>
                    <div class="form-group">
                        <label for="model">Model <span style="color:red;">*</span></label>
                        <input type="text" class="form-control" id="model" name="model" maxlength="50" required>
                    </div>
                    <div class="form-group">
                        <label for="manufactureYear">Manufacture Year <span style="color:red;">*</span></label>
                        <input type="number" class="form-control" id="manufactureYear" name="manufactureYear" min="1900" max="9999" required>
                    </div>
                    <div class="form-group">
                        <label for="engineNumber">Engine Number <span style="color:red;">*</span></label>
                        <input type="text" class="form-control" id="engineNumber" name="engineNumber" maxlength="100" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Vehicle</button>
                </form>
            </div>

            <!-- Tab: Schedule Inspection -->
            <div class="tab-pane fade show active" id="schedule-inspection" role="tabpanel">
                <h3>Schedule Inspection</h3>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success" role="alert">${successMessage}</div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </c:if>
                <form action="${pageContext.request.contextPath}/scheduleInspection" method="POST">
                    <div class="form-group">
                        <label for="vehicleId">Select Vehicle <span style="color:red;">*</span></label>
                        <select class="form-control" id="vehicleId" name="vehicleId" required>
                            <c:if test="${empty sessionScope.vehicles}">
                                <option value="">No vehicles available</option>
                            </c:if>
                            <c:forEach var="vehicle" items="${sessionScope.vehicles}">
                                <option value="${vehicle.vehicleID}">${vehicle.plateNumber} (${vehicle.brand} ${vehicle.model})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="stationId">Inspection Station <span style="color:red;">*</span></label>
                        <select class="form-control" id="stationId" name="stationId" required>
                            <c:if test="${empty applicationScope.inspectionStations}">
                                <option value="">No stations available</option>
                            </c:if>
                            <c:forEach var="station" items="${applicationScope.inspectionStations}">
                                <option value="${station.stationID}">${station.name} - ${station.address}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="inspectionDate">Inspection Date <span style="color:red;">*</span></label>
                        <input type="date" class="form-control" id="inspectionDate" name="inspectionDate" min="${java.time.LocalDate.now()}" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Schedule Inspection</button>
                </form>
            </div>

            <!-- Tab: Track History -->
            <div class="tab-pane fade" id="track-history" role="tabpanel">
                <h3>Inspection History</h3>
                <p>(Bảng này sẽ được cập nhật sau)</p>
            </div>

        </div>
    </div>

    <!-- JavaScript cho Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>