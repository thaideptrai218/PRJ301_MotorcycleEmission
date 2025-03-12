package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.UserDAO;
import model.User;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            // Email không tồn tại
            request.setAttribute("emailError", "This email is not registered.");
            request.getRequestDispatcher("/view/signin.jsp").forward(request, response);
        } else if (!user.getPassword().equals(password)) {
            // Mật khẩu sai
            request.setAttribute("passwordError", "Incorrect password. Please try again.");
            request.getRequestDispatcher("/view/signin.jsp").forward(request, response);
        } else {
            // Đăng nhập thành công
            HttpSession session = request.getSession();

            // Lưu userId và role vào session (thay vì toàn bộ đối tượng user)
            session.setAttribute("userId", user.getUserID());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("role", user.getRole());

            // Điều hướng đến trang JSP tương ứng dựa trên vai trò
            String role = user.getRole();
            String redirectUrl;

            switch (role) {
                case "Owner":
                    redirectUrl = "/view/secure/ownerDashboard.jsp";
                    break;
                case "Inspector":
                    redirectUrl = "/view/secure/inspectorDashboard.jsp";
                    break;
                case "Station":
                    redirectUrl = "/view/secure/stationDashboard.jsp";
                    break;
                case "Police":
                    redirectUrl = "/view/secure/policeDashboard.jsp";
                    break;
                default:
                    // Trường hợp vai trò không hợp lệ, chuyển về trang lỗi hoặc signin
                    redirectUrl = "/view/signin.jsp";
                    break;
            }
            
            // Chuyển hướng đến URL tương ứng
            System.out.println(request.getContextPath() + redirectUrl);
            response.sendRedirect(request.getContextPath() + redirectUrl);
        }

    }
}
