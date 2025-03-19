package controller.inspector;

import dao.InspectionScheduleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
            session.setAttribute("errorMessage", "Vui lòng chọn nơi làm việc trước!");
            response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
            return;
        }

        // Lấy tham số bộ lọc từ request
        String searchKeyword = request.getParameter("searchKeyword");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String status = request.getParameter("statusFilter");

        try {
            // Lấy danh sách lịch trình với bộ lọc
            List<InspectionSchedule> schedules = scheduleDao.getSchedulesByStationIdWithFilters(
                inspectionStationId, searchKeyword, fromDate, toDate, status
            );
            request.setAttribute("schedules", schedules);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("statusFilter", status);

            request.getRequestDispatcher("/view/secure/inspector/schedules.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách lịch: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/inspector/sidebar.jsp").forward(request, response);
        }
    }
}