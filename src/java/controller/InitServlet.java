package controller;

import dao.InspectionStationDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import java.util.List;
import model.InspectionStation;

public class InitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        InspectionStationDAO stationDAO = new InspectionStationDAO();
        List<InspectionStation> stations = stationDAO.getAllStations();
        context.setAttribute("inspectionStations", stations);
        System.out.println("Initialized inspectionStations with " + stations.size() + " records"); // Debug log
    }

}
