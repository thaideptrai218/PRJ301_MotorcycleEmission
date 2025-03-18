<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inspector Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/registerAndLogin.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
</head>
<body>
    <%@ include file="/view/header.jsp" %>

    <div class="container mt-4">
        <h2>Inspector Dashboard</h2>
        
        <!-- Form Ghi nhận kết quả kiểm tra -->
        <h3>Record Inspection Result</h3>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">${errorMessage}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/recordInspection" method="POST">
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
                <label for="co2Emission">CO2 Emission (g/km) <span style="color:red;">*</span></label>
                <input type="number" step="0.01" class="form-control" id="co2Emission" name="co2Emission" required>
            </div>
            <div class="form-group">
                <label for="hcEmission">HC Emission (g/km) <span style="color:red;">*</span></label>
                <input type="number" step="0.01" class="form-control" id="hcEmission" name="hcEmission" required>
            </div>
            <div class="form-group">
                <label for="result">Inspection Result <span style="color:red;">*</span></label>
                <select class="form-control" id="result" name="result" required>
                    <option value="Pass">Pass</option>
                    <option value="Fail">Fail</option>
                </select>
            </div>
            <div class="form-group">
                <label for="comments">Comments</label>
                <textarea class="form-control" id="comments" name="comments" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit Inspection</button>
        </form>
    </div>

    <!-- JavaScript cho Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
