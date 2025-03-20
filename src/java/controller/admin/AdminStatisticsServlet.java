package controller.admin;

import dao.StatisticsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "AdminStatisticsServlet", urlPatterns = {"/admin/statistics"})
public class AdminStatisticsServlet extends HttpServlet {

    private final StatisticsDAO statisticsDAO = new StatisticsDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Kiểm tra quyền Admin
            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("role");
            if (role == null || !role.equals("Admin")) {
                request.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này!");
                request.getRequestDispatcher("/view/login.jsp").forward(request, response);
                return;
            }

            // Lấy năm từ tham số (mặc định là năm hiện tại)
            int year = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : java.time.Year.now().getValue();

            // Lấy dữ liệu thống kê
            int totalUsers = statisticsDAO.countUsers();
            Map<String, Integer> usersByRole = statisticsDAO.countUsersByRole();
            int totalVehicles = statisticsDAO.countVehicles();
            int totalViolations = statisticsDAO.countViolations();
            Map<String, Integer> violationsByStatus = statisticsDAO.countViolationsByStatus();
            int totalInspectionRecords = statisticsDAO.countInspectionRecords();
            Map<String, Integer> inspectionsByResult = statisticsDAO.countInspectionRecordsByResult();
            int totalVerificationRecords = statisticsDAO.countVerificationRecords();
            Map<String, Integer> verificationsByStatus = statisticsDAO.countVerificationRecordsByStatus();
            int totalInspectionStations = statisticsDAO.countInspectionStations();
            int totalInspectionSchedules = statisticsDAO.countInspectionSchedules();
            Map<String, Integer> schedulesByStatus = statisticsDAO.countInspectionSchedulesByStatus();
            Map<String, Integer> violationsByMonth = statisticsDAO.getViolationsByMonth(year);
            Map<String, Integer> inspectionsByMonth = statisticsDAO.getInspectionRecordsByMonth(year);
            Map<String, Integer> verificationsByMonth = statisticsDAO.getVerificationRecordsByMonth(year);

            // Truyền dữ liệu sang JSP
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("usersByRole", usersByRole);
            request.setAttribute("totalVehicles", totalVehicles);
            request.setAttribute("totalViolations", totalViolations);
            request.setAttribute("violationsByStatus", violationsByStatus);
            request.setAttribute("totalInspectionRecords", totalInspectionRecords);
            request.setAttribute("inspectionsByResult", inspectionsByResult);
            request.setAttribute("totalVerificationRecords", totalVerificationRecords);
            request.setAttribute("verificationsByStatus", verificationsByStatus);
            request.setAttribute("totalInspectionStations", totalInspectionStations);
            request.setAttribute("totalInspectionSchedules", totalInspectionSchedules);
            request.setAttribute("schedulesByStatus", schedulesByStatus);
            request.setAttribute("violationsByMonth", violationsByMonth);
            request.setAttribute("inspectionsByMonth", inspectionsByMonth);
            request.setAttribute("verificationsByMonth", verificationsByMonth);
            request.setAttribute("selectedYear", year);
            request.getRequestDispatcher("/view/secure/admin/statistics.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải dữ liệu thống kê: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/admin/statistics.jsp").forward(request, response);
        }
    }
}