package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.VehicleDAO;
import model.Vehicle;

public class AddVehicleServlet extends HttpServlet {
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int ownerID = Integer.parseInt(request.getParameter("ownerID"));
        String plateNumber = request.getParameter("plateNumber");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        int manufactureYear = Integer.parseInt(request.getParameter("manufactureYear"));
        String engineNumber = request.getParameter("engineNumber");

        Vehicle vehicle = new Vehicle();
        vehicle.setOwnerID(ownerID);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setManufactureYear(manufactureYear);
        vehicle.setEngineNumber(engineNumber);

        try {
            boolean success = vehicleDAO.addVehicle(vehicle);
            if (success) {
                // Cập nhật danh sách xe trong session
                HttpSession session = request.getSession();
                session.setAttribute("vehicles", vehicleDAO.getVehiclesByOwnerId(ownerID));
                response.sendRedirect(request.getContextPath() + "/ownerDashboard");
            } else {
                request.setAttribute("errorMessage", "Failed to add vehicle. Please try again.");
                request.getRequestDispatcher("/view/secure/ownerDashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/ownerDashboard.jsp").forward(request, response);
        }
    }
}