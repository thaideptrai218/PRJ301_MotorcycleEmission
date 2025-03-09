<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Sign Up</title>

        <!-- Font Icon -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/material-icon/css/material-design-iconic-font.min.css">

        <!-- Main CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/registerAndLogin.css">
    </head>
    <body>

        <div class="main">
            <section class="signup">
                <div class="container">
                    <div class="signup-content">
                        <div class="signup-form">
                            <h2 class="form-title">Sign up</h2>
                            <form method="POST" action="${pageContext.request.contextPath}/register" class="register-form" id="register-form">

                                <!-- Name -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.name ? 'error' : ''}">
                                        <label for="name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                        <input type="text" name="name" id="name" placeholder="Your Name" value="${requestScope.name}"/>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.name}">
                                            <span class="error-message">${requestScope.errors.name}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Email -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.email ? 'error' : ''}">
                                        <label for="email"><i class="zmdi zmdi-email"></i></label>
                                        <input type="email" name="email" id="email" placeholder="Your Email" value="${requestScope.email}"/>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.email}">
                                            <span class="error-message">${requestScope.errors.email}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Phone -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.phone ? 'error' : ''}">
                                        <label for="phone"><i class="zmdi zmdi-phone"></i></label>
                                        <input type="text" name="phone" id="phone" placeholder="Your Phone" value="${requestScope.phone}"/>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.phone}">
                                            <span class="error-message">${requestScope.errors.phone}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Role -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.role ? 'error' : ''}">
                                        <label for="role"><i class="zmdi zmdi-account-box"></i></label>
                                        <select name="role" id="role">
                                            <option value="" disabled ${empty requestScope.role ? 'selected' : ''}>Select Your Role</option>
                                            <option value="Owner" ${requestScope.role == 'Owner' ? 'selected' : ''}>Owner</option>
                                            <option value="Inspector" ${requestScope.role == 'Inspector' ? 'selected' : ''}>Inspector</option>
                                            <option value="Station" ${requestScope.role == 'Station' ? 'selected' : ''}>Station</option>
                                            <option value="Police" ${requestScope.role == 'Police' ? 'selected' : ''}>Police</option>
                                        </select>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.role}">
                                            <span class="error-message">${requestScope.errors.role}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Password -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.pass ? 'error' : ''}">
                                        <label for="pass"><i class="zmdi zmdi-lock"></i></label>
                                        <input type="password" name="pass" id="pass" placeholder="Password"/>
                                        <span class="toggle-password" data-target="pass">
                                            <i class="zmdi zmdi-eye"></i>
                                        </span>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.pass}">
                                            <span class="error-message">${requestScope.errors.pass}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Confirm Password -->
                                <div class="form-group">
                                    <div class="input-wrapper ${not empty requestScope.errors.rePass ? 'error' : ''}">
                                        <label for="re_pass"><i class="zmdi zmdi-lock-outline"></i></label>
                                        <input type="password" name="re_pass" id="re_pass" placeholder="Repeat your password"/>
                                        <span class="toggle-password" data-target="re_pass">
                                            <i class="zmdi zmdi-eye"></i>
                                        </span>
                                    </div>
                                    <div class="error-container">
                                        <c:if test="${not empty requestScope.errors.rePass}">
                                            <span class="error-message">${requestScope.errors.rePass}</span>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Submit -->
                                <div class="form-group form-button">
                                    <input type="submit" name="signup" id="signup" class="form-submit" value="Register"/>
                                </div>
                            </form>
                        </div>

                        <!-- Sign up image -->
                        <div class="signup-image">
                            <figure><img src="${pageContext.request.contextPath}/assets/images/signup/signup-image.jpg" alt="sign up image"></figure>
                            <a href="${pageContext.request.contextPath}/view/signin.jsp" class="signup-image-link">I am already a member</a>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <!-- JS -->
        <script src="${pageContext.request.contextPath}/assets/vendor/jquery/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/register.js"></script>
    </body>
</html>