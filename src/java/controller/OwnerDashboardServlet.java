package controller;

import dao.VehicleDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
public class OwnerDashboardServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null || !"Owner".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/view/signin.jsp");
            return;
        }

        response.sendRedirect("owner/home");
    }
}
