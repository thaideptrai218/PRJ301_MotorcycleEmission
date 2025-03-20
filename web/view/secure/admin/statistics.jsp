<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê và báo cáo - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="container mt-4">
        <!-- Header và Sidebar -->
        <%@ include file="/view/secure/admin/header.jsp" %>
        <%@ include file="/view/secure/admin/sidebar.jsp" %>

        <div class="content">
            <div class="container mt-4">
                <div class="card p-4 shadow">
                    <h3>Thống kê và báo cáo <i class="fas fa-chart-bar"></i></h3>

                    <!-- Hiển thị thông báo lỗi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </c:if>

                    <!-- Thống kê tổng quan -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="card text-white bg-primary">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số người dùng</h5>
                                    <p class="card-text">${totalUsers}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card text-white bg-success">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số xe</h5>
                                    <p class="card-text">${totalVehicles}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card text-white bg-warning">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số vi phạm</h5>
                                    <p class="card-text">${totalViolations}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card text-white bg-info">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số kiểm định</h5>
                                    <p class="card-text">${totalInspectionRecords}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="card text-white bg-secondary">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số xác minh</h5>
                                    <p class="card-text">${totalVerificationRecords}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card text-white bg-dark">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số trạm kiểm định</h5>
                                    <p class="card-text">${totalInspectionStations}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card text-white bg-danger">
                                <div class="card-body">
                                    <h5 class="card-title">Tổng số lịch kiểm định</h5>
                                    <p class="card-text">${totalInspectionSchedules}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Thống kê chi tiết -->
                    <div class="row mb-4">
                        <div class="col-md-4">
                            <h5>Người dùng theo vai trò</h5>
                            <canvas id="usersByRoleChart"></canvas>
                        </div>
                        <div class="col-md-4">
                            <h5>Vi phạm theo trạng thái</h5>
                            <canvas id="violationsByStatusChart"></canvas>
                        </div>
                        <div class="col-md-4">
                            <h5>Kiểm định theo kết quả</h5>
                            <canvas id="inspectionsByResultChart"></canvas>
                        </div>
                    </div>

                    <div class="row mb-4">
                        <div class="col-md-6">
                            <h5>Xác minh phương tiện theo trạng thái</h5>
                            <canvas id="verificationsByStatusChart"></canvas>
                        </div>
                        <div class="col-md-6">
                            <h5>Lịch kiểm định theo trạng thái</h5>
                            <canvas id="schedulesByStatusChart"></canvas>
                        </div>
                    </div>

                    <!-- Form chọn năm -->
                    <form action="${pageContext.request.contextPath}/admin/statistics" method="get" class="mb-3">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="year">Chọn năm:</label>
                                <select name="year" id="year" class="form-control" onchange="this.form.submit()">
                                    <c:forEach begin="2020" end="2030" var="y">
                                        <option value="${y}" ${selectedYear == y ? 'selected' : ''}>${y}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </form>

                    <!-- Biểu đồ theo tháng -->
                    <div class="row">
                        <div class="col-md-4">
                            <h5>Số lượng vi phạm theo tháng (${selectedYear})</h5>
                            <canvas id="violationsChart"></canvas>
                        </div>
                        <div class="col-md-4">
                            <h5>Số lượng kiểm định theo tháng (${selectedYear})</h5>
                            <canvas id="inspectionsChart"></canvas>
                        </div>
                        <div class="col-md-4">
                            <h5>Số lượng xác minh theo tháng (${selectedYear})</h5>
                            <canvas id="verificationsChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Biểu đồ người dùng theo vai trò
        const usersByRoleData = {
            labels: ['Owner', 'Inspector', 'Station', 'Police', 'Admin'],
            datasets: [{
                label: 'Số lượng',
                data: [
                    ${usersByRole['Owner']}, ${usersByRole['Inspector']}, ${usersByRole['Station']},
                    ${usersByRole['Police']}, ${usersByRole['Admin']}
                ],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('usersByRoleChart'), {
            type: 'pie',
            data: usersByRoleData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                }
            }
        });

        // Biểu đồ vi phạm theo trạng thái
        const violationsByStatusData = {
            labels: ['Pending', 'Resolved'],
            datasets: [{
                label: 'Số lượng',
                data: [
                    ${violationsByStatus['Pending']}, ${violationsByStatus['Resolved']}
                ],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)'
                ],
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('violationsByStatusChart'), {
            type: 'pie',
            data: violationsByStatusData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                }
            }
        });

        // Biểu đồ kiểm định theo kết quả
        const inspectionsByResultData = {
            labels: ['Pass', 'Fail'],
            datasets: [{
                label: 'Số lượng',
                data: [
                    ${inspectionsByResult['Pass']}, ${inspectionsByResult['Fail']}
                ],
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 99, 132, 0.2)'
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 99, 132, 1)'
                ],
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('inspectionsByResultChart'), {
            type: 'pie',
            data: inspectionsByResultData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                }
            }
        });

        // Biểu đồ xác minh phương tiện theo trạng thái
        const verificationsByStatusData = {
            labels: ['Pending', 'Approved', 'Rejected'],
            datasets: [{
                label: 'Số lượng',
                data: [
                    ${verificationsByStatus['Pending']}, ${verificationsByStatus['Approved']}, ${verificationsByStatus['Rejected']}
                ],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 99, 132, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 99, 132, 1)'
                ],
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('verificationsByStatusChart'), {
            type: 'pie',
            data: verificationsByStatusData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                }
            }
        });

        // Biểu đồ lịch kiểm định theo trạng thái
        const schedulesByStatusData = {
            labels: ['Pending', 'Confirmed', 'Cancelled', 'Completed'],
            datasets: [{
                label: 'Số lượng',
                data: [
                    ${schedulesByStatus['Pending']}, ${schedulesByStatus['Confirmed']},
                    ${schedulesByStatus['Cancelled']}, ${schedulesByStatus['Completed']}
                ],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(75, 192, 192, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 99, 132, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('schedulesByStatusChart'), {
            type: 'pie',
            data: schedulesByStatusData,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                }
            }
        });

        // Biểu đồ vi phạm theo tháng
        const violationsData = {
            labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
            datasets: [{
                label: 'Số lượng vi phạm',
                data: [
                    ${violationsByMonth['1']}, ${violationsByMonth['2']}, ${violationsByMonth['3']},
                    ${violationsByMonth['4']}, ${violationsByMonth['5']}, ${violationsByMonth['6']},
                    ${violationsByMonth['7']}, ${violationsByMonth['8']}, ${violationsByMonth['9']},
                    ${violationsByMonth['10']}, ${violationsByMonth['11']}, ${violationsByMonth['12']}
                ],
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('violationsChart'), {
            type: 'bar',
            data: violationsData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Biểu đồ kiểm định theo tháng
        const inspectionsData = {
            labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
            datasets: [{
                label: 'Số lượng kiểm định',
                data: [
                    ${inspectionsByMonth['1']}, ${inspectionsByMonth['2']}, ${inspectionsByMonth['3']},
                    ${inspectionsByMonth['4']}, ${inspectionsByMonth['5']}, ${inspectionsByMonth['6']},
                    ${inspectionsByMonth['7']}, ${inspectionsByMonth['8']}, ${inspectionsByMonth['9']},
                    ${inspectionsByMonth['10']}, ${inspectionsByMonth['11']}, ${inspectionsByMonth['12']}
                ],
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('inspectionsChart'), {
            type: 'bar',
            data: inspectionsData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Biểu đồ xác minh theo tháng
        const verificationsData = {
            labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
            datasets: [{
                label: 'Số lượng xác minh',
                data: [
                    ${verificationsByMonth['1']}, ${verificationsByMonth['2']}, ${verificationsByMonth['3']},
                    ${verificationsByMonth['4']}, ${verificationsByMonth['5']}, ${verificationsByMonth['6']},
                    ${verificationsByMonth['7']}, ${verificationsByMonth['8']}, ${verificationsByMonth['9']},
                    ${verificationsByMonth['10']}, ${verificationsByMonth['11']}, ${verificationsByMonth['12']}
                ],
                backgroundColor: 'rgba(255, 206, 86, 0.2)',
                borderColor: 'rgba(255, 206, 86, 1)',
                borderWidth: 1
            }]
        };
        new Chart(document.getElementById('verificationsChart'), {
            type: 'bar',
            data: verificationsData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    </script>
</body>
</html>