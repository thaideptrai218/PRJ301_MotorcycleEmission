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
            request.setAttribute("emailError", "This email is not registered.");
            request.getRequestDispatcher("/view/signin.jsp").forward(request, response);
        } else if (!user.getPassword().equals(password)) {
            request.setAttribute("passwordError", "Incorrect password. Please try again.");
            request.getRequestDispatcher("/view/signin.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

    }
}
