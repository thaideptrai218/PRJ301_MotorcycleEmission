<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Track History - Owner Dashboard</title>
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
                <h2>Track History</h2>
                <div id="track-history">
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
                    <c:if test="${not empty inspectionRecords}">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Plate Number</th>
                                    <th>Name</th>
                                    <th>Inspection Date</th>
                                    <th>Result</th>
                                    <th>CO2 Emission (g/km)</th>
                                    <th>HC Emission (g/km)</th>
                                    <th>Expiration Date</th>
                                    <th>Status</th>
                                    <th>Comments</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="record" items="${inspectionRecords}">
                                    <tr>
                                        <td>${record.plateNumber}</td>
                                        <td>${record.stationName}</td>
                                        <td><fmt:formatDate value="${record.inspectionDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                        <td>${record.result}</td>
                                        <td>${record.co2Emission}</td>
                                        <td>${record.hcEmission}</td>
                                        <td><fmt:formatDate value="${record.expirationDate}" pattern="dd/MM/yyyy" /></td>
                                        <td>${record.status}</td>
                                        <td>${record.comments}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                    <c:if test="${empty inspectionRecords}">
                        <p class="status-pending"><i class="fas fa-info-circle"></i> Không có lịch sử kiểm định nào.</p>
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