<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Vehicles - Owner Dashboard</title>
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
                    <h2>Vehicles</h2>
                    <div class="" id="vehicles">
                        <h3>My Vehicles <i class="fas fa-car"></i></h3>
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

                        <c:if test="${not empty vehicles and not empty vehicles}">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Vehicle ID</th>
                                        <th>Plate Number</th>
                                        <th>Brand</th>
                                        <th>Model</th>
                                        <th>Manufacture Year</th>
                                        <th>Engine Number</th>
                                        <th>Verification Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="vehicle" items="${vehicles}">
                                        <tr>
                                            <td>${vehicle.vehicleID}</td>
                                            <td>${vehicle.plateNumber}</td>
                                            <td>${vehicle.brand}</td>
                                            <td>${vehicle.model}</td>
                                            <td>${vehicle.manufactureYear}</td>
                                            <td>${vehicle.engineNumber}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${vehicle.verificationStatus == 'Approved'}">
                                                        <span class="status-success">Approved</span>
                                                    </c:when>
                                                    <c:when test="${vehicle.verificationStatus == 'Pending'}">
                                                        <span class="status-pending">Pending</span>
                                                    </c:when>
                                                    <c:when test="${vehicle.verificationStatus == 'Rejected'}">
                                                        <span class="status-error">Rejected</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-error">Not Verified</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty vehicles or empty vehicles}">
                            <p class="status-pending"><i class="fas fa-info-circle"></i> Bạn chưa đăng ký phương tiện nào.</p>
                        </c:if>
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