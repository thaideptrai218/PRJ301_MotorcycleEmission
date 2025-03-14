<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Owner Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="0">

    </head>
    <body>
        <div class="wrapper">
            <%@ include file="/view/header.jsp" %>

            <!-- Sidebar -->
            <div class="sidebar" id="sidebar">
                <div class="user-info">
                    <div class="user-info-avatar">
                        <img src="${pageContext.request.contextPath}/assets/images/default-avatar.jpg" alt="Avatar" class="rounded-circle mb-2" width="50" height="50">
                    </div>
                    <div class="user-info-avatardesc">
                        <h5>${sessionScope.userName}</h5>
                        <p>${sessionScope.userEmail}</p>
                        <p>Vai trò: ${sessionScope.role}</p>
                        <p>SĐT: ${sessionScope.userPhone}</p>
                    </div>
                </div>
                <hr class="bg-light">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="#home" data-toggle="tab"><i class="fas fa-home"></i> Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#add-vehicle" data-toggle="tab"><i class="fas fa-car"></i> Add Vehicle</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#schedule-inspection" data-toggle="tab"><i class="fas fa-file-alt"></i> Schedule Inspection</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#track-history" data-toggle="tab"><i class="fas fa-history"></i> Track History</a>
                    </li>
                </ul>
                <div class="logout-section">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-block">Đăng xuất</a>
                </div>
            </div>

            <!-- Main Content -->
            <div class="content">
                <div class="container mt-4">
                    <h2>Owner Dashboard</h2>

                    <!-- Tab Content -->
                    <div class="tab-content mt-3" id="myTabContent">
                        <!-- Tab: Home -->
                        <div class="tab-pane fade show active" id="home" role="tabpanel">
                            <h3>Home <i class="fas fa-smile"></i></h3>
                            <p>Chào mừng đến với Owner Dashboard.</p>
                        </div>

                        <!-- Tab: Add Vehicle -->
                        <div class="tab-pane fade" id="add-vehicle" role="tabpanel">
                            <h3>Add New Vehicle <i class="fas fa-plus"></i></h3>
                                <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger status-error" role="alert">
                                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                                </div>
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
                        <div class="tab-pane fade" id="schedule-inspection" role="tabpanel">
                            <h3>Schedule Inspection <i class="fas fa-calendar"></i></h3>
                                <c:if test="${not empty successMessage}">
                                <div class="alert alert-success status-success" role="alert">
                                    <i class="fas fa-check-circle"></i> ${successMessage}
                                </div>
                            </c:if>
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger status-error" role="alert">
                                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                                </div>
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
                            <h3>Track History <i class="fas fa-clock"></i></h3>
                            <p class="status-pending"><i class="fas fa-clock"></i> (Bảng này đang được cập nhật)</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>






        <!-- JavaScript -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            $(document).ready(function () {
                // Toggle sidebar
                $('#sidebarToggle').on('click', function () {
                    $('#sidebar').toggleClass('active');
                    if ($('#sidebar').hasClass('active')) {
                        $('.sidebar').css('display', 'block');
                    } else {
                        $('.sidebar').css('display', 'none');
                    }
                });

                // Tab navigation
                $('.nav-link').on('click', function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                });
            });
        </script>
    </body>
</html>