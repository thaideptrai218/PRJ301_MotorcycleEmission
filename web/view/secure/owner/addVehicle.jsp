<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Vehicle - Owner Dashboard</title>
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
                    <h2>Add Vehicle</h2>
                    <div class="" id="add-vehicle">
                        <h3>Add New Vehicle <i class="fas fa-plus"></i></h3>
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
                        <form action="${pageContext.request.contextPath}/owner/addVehicle" method="POST">
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
                </div>
            </div>

            <!-- JavaScript -->
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/js/hambergurButton.js"></script>
        </div>
    </body>
</html>