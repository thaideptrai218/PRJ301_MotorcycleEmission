<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Sign In</title>

        <!-- Base URL to fix relative paths -->
        <base href="${pageContext.request.contextPath}/">

        <!-- Font Icon -->
        <link rel="stylesheet" href="assets/fonts/material-icon/css/material-design-iconic-font.min.css">

        <!-- Main CSS -->
        <link rel="stylesheet" href="assets/css/registerAndLogin.css">

        <meta name="robots" content="noindex, follow">
    </head>
    <body>

        <div class="main">
            <section class="sign-in">
                <div class="container">
                    <div class="signin-content">
                        <div class="signin-image">
                            <figure>
                                <img src="assets/images/signup/signin-image.jpg" alt="Sign in image">
                            </figure>
                            <a href="view/register.jsp" class="signup-image-link">Create an account</a>
                        </div>

                        <div class="signin-form">
                            <h2 class="form-title">Sign In</h2>

                            <c:if test="${not empty successMessage}">
                                <div class="success-message">${successMessage}</div>
                            </c:if>

                            <!-- Show Error Message if login fails -->
                            <c:if test="${not empty loginError}">
                                <div class="error-message">
                                    <p style="color: red; text-align: center;">${loginError}</p>
                                </div>
                            </c:if>

                            <form method="POST" class="register-form" id="login-form" action="${pageContext.request.contextPath}/login">
                                <div class="form-group">
                                    <div class="input-wrapper">
                                        <label for="email"><i class="zmdi zmdi-email"></i></label>
                                        <input type="text" name="email" id="email" placeholder="Your Email" value="${param.email}"/>
                                    </div>
                                     <c:if test="${not empty emailError}">
                                        <div class="error-message">${emailError}</div>
                                    </c:if>
                                </div>

                                <div class="form-group">    
                                    <div class="input-wrapper">
                                        <label for="password"><i class="zmdi zmdi-lock"></i></label>
                                        <input type="password" name="password" id="password" placeholder="Password"/>
                                        <span class="toggle-password" data-target="password">
                                            <i class="zmdi zmdi-eye"></i>
                                        </span>
                                    </div>
                                    <c:if test="${not empty passwordError}">
                                        <div class="error-message">${passwordError}</div>
                                    </c:if>
                                </div>

                                <div class="form-group form-button">
                                    <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <!-- JS -->
        <script src="assets/vendor/jquery/jquery.min.js"></script>
        <script src="assets/js/signin.js"></script>

    </body>
</html>
