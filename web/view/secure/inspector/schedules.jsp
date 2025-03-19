<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Schedules - Inspector Dashboard</title>
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
                    <h2>Schedules</h2>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>

                    <c:if test="${not empty schedules}">
                        <table class="table table-bordered">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Schedule ID</th>
                                    <th>Vehicle ID</th>
                                    <th>Station ID</th>
                                    <th>Owner ID</th>
                                    <th>Created Date</th>
                                    <th>Schedule Date</th>
                                    <th>Select</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="schedule" items="${schedules}">
                                    <tr>
                                        <td>${schedule.scheduleID}</td>
                                        <td>${schedule.vehicleID}</td>
                                        <td>${schedule.stationID}</td>
                                        <td>${schedule.ownerID}</td>
                                        <td>${schedule.createdAt}</td>
                                        <td>${schedule.scheduleDate}</td>
                                        <td><a href="${pageContext.request.contextPath}/inspector/inspectionSchedule?vehicleID=${schedule.vehicleID}&stationID=${schedule.stationID}">Select</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </c:if>
                    <c:if test="${empty schedules}">
                        <p class="text-warning"><i class="fas fa-info-circle"></i> Chưa có lịch trình nào.</p>
                    </c:if>
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
