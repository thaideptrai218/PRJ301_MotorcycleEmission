<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Home - Owner Dashboard</title>
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/assets/css/dashboard.css"
            type="text/css"
        />
        <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
        />
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
            integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
        />
    </head>
    <body>
        <div class="wrapper">
            <%@ include file="/view/secure/station/header.jsp" %> <%@ include
            file="/view/secure/station/sidebar.jsp" %>

            <!-- Main Content -->
            <div class="content">
                <div class="container mt-4">
                    <h2>Home</h2>
                    <div
                        class="tab-pane fade show active"
                        id="home"
                        role="tabpanel"
                    >
                        <h3>Home <i class="fas fa-smile"></i></h3>
                        <p>Chào mừng đến với Inspector Dashboard.</p>
                        <c:if test="${not empty successMessage}">
                            <div
                                class="alert alert-success status-success"
                                role="alert"
                            >
                                <i class="fas fa-check-circle"></i>
                                ${successMessage}
                            </div>
                        </c:if>
                        <c:if test="${not empty errorMessage}">
                            <div
                                class="alert alert-danger status-error"
                                role="alert"
                            >
                                <i class="fas fa-exclamation-circle"></i>
                                ${errorMessage}
                            </div>
                        </c:if>
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
