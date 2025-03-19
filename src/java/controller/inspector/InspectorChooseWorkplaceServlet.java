package controller.inspector;

import dao.InspectionStationDAO;
import model.InspectionStation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "InspectorChooseWorkplaceServlet", urlPatterns = {"/inspector/chooseWorkplace"})
public class InspectorChooseWorkplaceServlet extends HttpServlet {
    private final InspectionStationDAO inspectionStationDAO = new InspectionStationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        try {
            // Lấy danh sách tất cả InspectionStation
            List<InspectionStation> availableStations = inspectionStationDAO.getAllStations();
            if (availableStations.isEmpty()) {
                session.setAttribute("errorMessage", "Không có trạm kiểm định nào trong hệ thống.");
                response.sendRedirect(request.getContextPath() + "/inspector/home");
                return;
            }
            request.setAttribute("availableStations", availableStations);
            request.getRequestDispatcher("/view/secure/inspector/chooseWorkplace.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải danh sách nơi làm việc: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inspector/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // Kiểm tra đăng nhập và vai trò
        if (userId == null || role == null || (!role.equals("Station") && !role.equals("Inspector"))) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập với vai trò Station hoặc Inspector.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String stationIdStr = request.getParameter("stationId");
        if (stationIdStr == null || stationIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng chọn nơi làm việc.");
            response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
            return;
        }

        int stationId;
        try {
            stationId = Integer.parseInt(stationIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID nơi làm việc không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
            return;
        }

        try {
            // Kiểm tra xem InspectionStation có tồn tại không
            InspectionStation station = inspectionStationDAO.getStationById(stationId);
            if (station == null) {
                session.setAttribute("errorMessage", "Nơi làm việc không tồn tại.");
                response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
                return;
            }

            // Lưu InspectionStationID vào session
            session.setAttribute("inspectionStationId", stationId);
            session.setAttribute("successMessage", "Nơi làm việc '" + station.getName() + "' đã được chọn thành công.");
            response.sendRedirect(request.getContextPath() + "/inspector/home");

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi lưu nơi làm việc: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inspector/chooseWorkplace");
        }
    }
}