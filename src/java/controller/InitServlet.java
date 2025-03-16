package controller;

import dao.InspectionStationDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import java.sql.SQLException;
import java.util.List;
import model.InspectionStation;

public class InitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        InspectionStationDAO stationDAO = new InspectionStationDAO();
        try {
            List<InspectionStation> stations = stationDAO.getAllStations();
            context.setAttribute("inspectionStations", stations);
        System.out.println("Initialized inspectionStations with " + stations.size() + " records"); // Debug log
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        
    }

}
