<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Station Requests</title>
C
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <div class="wrapper">
            <%@ include file="/view/secure/station/header.jsp" %>
            <%@ include file="/view/secure/station/sidebar.jsp" %>

            <div class="content">
                <div class="container mt-4">
                    <h2>Pending Requests</h2>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                        <c:remove var="errorMessage" scope="session" />
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <c:choose>
                        <c:when test="${empty requests}">
                            <p class="text-muted">Không có yêu cầu nào.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Request ID</th>
                                        <th>Type</th>
                                        <th>Vehicle ID</th>
                                        <th>Created By</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="request" items="${requests}">
                                        <tr>
                                            <td>${request.requestID}</td>
                                            <td>${request.type}</td>
                                            <td>${request.vehicleID}</td>
                                            <td>${request.createdBy}</td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/station/requests">
                                                    <input type="hidden" name="requestId" value="${request.requestID}">
                                                    <button type="submit" name="action" value="approve" class="btn btn-success btn-sm">Approve</button>
                                                    <button type="submit" name="action" value="reject" class="btn btn-danger btn-sm">Reject</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
    </body>
</html>