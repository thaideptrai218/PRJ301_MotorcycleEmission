package controller;

import exception.UserAlreadyExistsException;
import service.UserService;
import model.User;
import utils.Validator;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String password = request.getParameter("pass");
        String confirmPassword = request.getParameter("re_pass");

        HashMap<String, String> errors = new HashMap<>();

        // Kiểm tra lỗi nhập liệu
        addError(errors, "name", name, Validator.isValidName(name), "Invalid name format! Only letters allowed.");
        addError(errors, "email", email, Validator.isValidEmail(email), "Invalid email address format!");
        addError(errors, "phone", phone, Validator.isValidPhone(phone), "Phone number must be 10-15 digits!");
        addError(errors, "role", role, role != null && !role.isEmpty(), "Please select a valid role.");
        addError(errors, "password", password, password != null && password.length() >= 6, "Password must be at least 6 characters!");
        addError(errors, "re_pass", confirmPassword, password.equals(confirmPassword), "Passwords do not match!");

        if (!errors.isEmpty()) {
            forwardToRegisterPage(request, response, errors, name, email, phone, role);
            return;
        }

        // Đăng ký người dùng
        User user = new User(0, name, email, password, role, phone);

        try {
            userService.registerUser(user);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (UserAlreadyExistsException e) {
            errors.put(e.getField(), e.getMessage());
            forwardToRegisterPage(request, response, errors, name, email, phone, role);
        }
    }

    private void forwardToRegisterPage(HttpServletRequest request, HttpServletResponse response,
            Map<String, String> errors, String name, String email, String phone, String role)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("role", role);
        request.getRequestDispatcher("/view/register.jsp").forward(request, response);
    }

    private void addError(HashMap<String, String> errors, String field, String value, boolean isValid, String errorMessage) {
        if (!isValid) {
            errors.put(field, errorMessage);
        }
    }
}
