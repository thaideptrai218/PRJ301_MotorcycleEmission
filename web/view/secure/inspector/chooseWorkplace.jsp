<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Choose Workplace</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        <%@ include file="/view/secure/inspector/header.jsp" %>
        <%@ include file="/view/secure/inspector/sidebar.jsp" %>
        <div class="content">

            <div class="container mt-4">
                <h2>Chọn Nơi Làm Việc</h2>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">${successMessage}</div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/inspector/chooseWorkplace">
                    <div class="form-group">
                        <label for="stationId">Chọn nơi làm việc:</label>
                        <select name="stationId" class="form-control" required>
                            <c:forEach var="station" items="${availableStations}">
                                <option value="${station.stationID}">${station.name} - ${station.address}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Xác nhận</button>
                </form>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
    </body>
</html>