package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class OwnerServletUtils {
    public static void redirectToPage(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
        response.sendRedirect(request.getContextPath() + page);
    }

    public static void setErrorMessage(HttpSession session, String message) {
        session.setAttribute("errorMessage", message);
    }

    public static void setSuccessMessage(HttpSession session, String message) {
        session.setAttribute("successMessage", message);
    }

    public static Integer getOwnerId(HttpSession session) {
        return (Integer) session.getAttribute("userId");
    }
}