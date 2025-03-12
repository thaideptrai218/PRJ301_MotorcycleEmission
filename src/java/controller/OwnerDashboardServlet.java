package controller;

import dao.VehicleDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Vehicle;

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

        int userId = (int) session.getAttribute("userId");
        List<Vehicle> vehicles = vehicleDAO.getVehiclesByOwnerId(userId);
        session.setAttribute("vehicles", vehicles);

        request.getRequestDispatcher("/view/secure/ownerDashboard.jsp").forward(request, response);
    }
}
