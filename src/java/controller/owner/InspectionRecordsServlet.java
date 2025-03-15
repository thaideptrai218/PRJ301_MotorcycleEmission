package controller.owner;

import dao.InspectionRecordDAO;
import model.InspectionRecord;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="InspectionRecordsServlet", urlPatterns={"/owner/inspectionRecords"})
public class InspectionRecordsServlet extends HttpServlet {
    
    private final InspectionRecordDAO inspectionRecordDAO = new InspectionRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        if (ownerId == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String scheduleIdStr = request.getParameter("scheduleId");
        if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Thiếu thông tin lịch hẹn.");
            response.sendRedirect(request.getContextPath() + "/owner/inspectionRequests");
            return;
        }

        int scheduleId;
        try {
            scheduleId = Integer.parseInt(scheduleIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID lịch hẹn không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/inspectionRequests");
            return;
        }

        try {
            List<InspectionRecord> inspectionRecords = inspectionRecordDAO.getInspectionRecordsByScheduleId(scheduleId);
            request.setAttribute("inspectionRecords", inspectionRecords);
            request.getRequestDispatcher("/view/secure/owner/inspectionRecords.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Lỗi khi tải kết quả kiểm định: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/inspectionRequests");
        }
    }
}