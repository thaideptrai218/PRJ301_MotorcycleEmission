package controller.inspector;

import dao.InspectionScheduleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.InspectionSchedule;

@WebServlet(name = "ScheduleFormPageServlet", urlPatterns = {"/inspector/scheduleFormPage"})
public class ScheduleFormPageServlet extends HttpServlet {

    private final InspectionScheduleDAO scheduleDao = new InspectionScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer inspectionStationId = (Integer) session.getAttribute("inspectionStationId");
        
        if (inspectionStationId == null) {
            session.setAttribute("errorMessage", "Please choose a workplace first!");
            response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
            return;
        }
        try {
            List<InspectionSchedule> schedules = scheduleDao.getConfirmedSchedulesByStationId(inspectionStationId);
            request.setAttribute("schedules", schedules);

            request.getRequestDispatcher("/view/secure/inspector/schedules.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading schedules: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/inspector/sidebar.jsp").forward(request, response);
        }
    }

}
