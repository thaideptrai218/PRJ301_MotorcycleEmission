<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inspection Records</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Inspection Records</h2>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>

        <c:choose>
            <c:when test="${empty inspectionRecords}">
                <p class="text-muted">Không có kết quả kiểm định nào cho lịch hẹn này.</p>
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Record ID</th>
                            <th>Inspection Date</th>
                            <th>Result</th>
                            <th>CO2 Emission</th>
                            <th>HC Emission</th>
                            <th>Comments</th>
                            <th>Expiration Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="record" items="${inspectionRecords}">
                            <tr>
                                <td>${record.recordID}</td>
                                <td><fmt:formatDate value="${record.inspectionDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                <td>${record.result}</td>
                                <td>${record.co2Emission}</td>
                                <td>${record.hcEmission}</td>
                                <td>${record.comments}</td>
                                <td><fmt:formatDate value="${record.expirationDate}" pattern="dd/MM/yyyy" /></td>
                                <td>${record.status}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/owner/inspectionRequests" class="btn btn-primary">Quay lại</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>