package controller.owner;

import dao.InspectionStationDAO;
import dao.VehicleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.InspectionStation;
import model.Vehicle;

public class ScheduleInspectionPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InspectionStationDAO stationDAO = new InspectionStationDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        HttpSession session = request.getSession(false);
        try {
            int userId = (int) session.getAttribute("userId");
            List<InspectionStation> stations = stationDAO.getAllStations();
            request.setAttribute("inspectionStations", stations);
            List<Vehicle> vehicles = vehicleDAO.getApprovedVehiclesByOwnerId(userId);
            request.setAttribute("vehicles", vehicles);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        request.getRequestDispatcher("/view/secure/owner/scheduleInspection.jsp").forward(request, response);
    }
}
