<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Schedule Inspection - Owner Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <div class="wrapper">
            <%@ include file="/view/secure/owner/header.jsp" %>
            <%@ include file="/view/secure/owner/sidebar.jsp" %>

            <!-- Main Content -->
            <div class="content">
                <div class="container mt-4">
                    <h2>Schedule Inspection</h2>
                    <div class="" id="schedule-inspection">
                        <h3>Schedule Inspection <i class="fas fa-calendar"></i></h3>
                            <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger status-error" role="alert">
                                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                            </div>
                            <c:set var="errorMessageDisplayed" value="true" scope="request" />
                        </c:if>
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success status-success" role="alert">
                                <i class="fas fa-check-circle"></i> ${successMessage}
                            </div>
                            <c:set var="successMessageDisplayed" value="true" scope="request" />
                        </c:if>
                        <form action="${pageContext.request.contextPath}/owner/scheduleInspection" method="POST">
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
                </div>
            </div>

            <!-- JavaScript -->
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                $(document).ready(function () {
                    $('#sidebarToggle').on('click', function () {
                        $('#sidebar').toggleClass('active');
                        if ($('#sidebar').hasClass('active')) {
                            $('.sidebar').css('display', 'block');
                        } else {
                            $('.sidebar').css('display', 'none');
                        }
                    });
                });
            </script>
        </div>
    </body>
</html>