package controller.owner;

import dao.InspectionRecordDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.InspectionRecord;

public class TrackHistoryPageServlet extends HttpServlet {

    private final InspectionRecordDAO inspectionRecordDAO = new InspectionRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        try {
            // Lấy danh sách lịch sử kiểm định
            List<InspectionRecord> inspectionRecords = inspectionRecordDAO.getInspectionRecordsByOwnerId(ownerId);
            request.setAttribute("inspectionRecords", inspectionRecords);
            request.getRequestDispatcher("/view/secure/owner/trackHistory.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải lịch sử kiểm định: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/trackHistoryPage");
        }
    }
}
