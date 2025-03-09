<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Sign In Form by Colorlib - Improved</title>

        <!-- Font Icon -->
        <link rel="stylesheet" href="../assets/fonts/material-icon/css/material-design-iconic-font.min.css">

        <!-- Main css -->
        <link rel="stylesheet" href="../assets/css/registerAndLogin.css">
        <meta name="robots" content="noindex, follow">
    </head>
    <body>

        <div class="main">

            <section class="sign-in">
                <div class="container">
                    <div class="signin-content">
                        <div class="signin-image">
                            <figure><img src="../assets/images/signup/signin-image.jpg" alt="sign in image"></figure>
                            <a href="register.jsp" class="signup-image-link">Create an account</a>
                        </div>

                        <div class="signin-form">
                            <h2 class="form-title">Sign In</h2>
                            <form method="POST" class="register-form" id="login-form">
                                <div class="form-group">
                                    <div class="input-wrapper">
                                        <label for="your_name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                        <input type="text" name="your_name" id="your_name" placeholder="Your Name"/>
                                    </div>
                                    <div class="error-container"></div>
                                </div>

                                <div class="form-group">    
                                    <div class="input-wrapper">
                                        <label for="your_pass"><i class="zmdi zmdi-lock"></i></label>
                                        <input type="password" name="your_pass" id="your_pass" placeholder="Password"/>
                                        <span class="toggle-password" data-target="your_pass">
                                            <i class="zmdi zmdi-eye"></i>
                                        </span>
                                    </div>
                                    <div class="error-container"></div>
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
        <script src="../assets/vendor/jquery/"></script>
        <script src="../assets/js/signin.js"></script>

        <!-- Toggle password visibility -->


        <!-- Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-23581568-13"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());
            gtag('config', 'UA-23581568-13');
        </script>

    </body>
</html>
