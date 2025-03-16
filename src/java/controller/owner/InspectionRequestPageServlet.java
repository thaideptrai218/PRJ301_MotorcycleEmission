package controller.owner;

import dao.InspectionScheduleDAO;
import model.InspectionSchedule;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class InspectionRequestPageServlet extends HttpServlet {
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        if (ownerId == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy danh sách Inspection Schedules của Owner
            List<InspectionSchedule> inspectionSchedules = inspectionScheduleDAO.getScheduleByUserID(ownerId);
            request.setAttribute("inspectionSchedules", inspectionSchedules);

            // Forward đến trang inspectionrequest.jsp
            request.getRequestDispatcher("/view/secure/owner/inspectionRequest.jsp").forward(request, response);
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}