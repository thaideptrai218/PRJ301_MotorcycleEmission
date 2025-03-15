package controller.owner;

import dao.InspectionScheduleDAO;
import dao.VerificationDAO;
import dao.NotificationDAO;
import dao.LogDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ScheduleInspectionServlet extends HttpServlet {
    private final InspectionScheduleDAO scheduleDAO = new InspectionScheduleDAO();
    private final VerificationDAO verificationDAO = new VerificationDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final LogDAO logDAO = new LogDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        // Lấy dữ liệu từ form
        String vehicleIdStr = request.getParameter("vehicleId");
        String stationIdStr = request.getParameter("stationId");
        String inspectionDate = request.getParameter("inspectionDate");

        // Xác thực dữ liệu đầu vào
        if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty() ||
            stationIdStr == null || stationIdStr.trim().isEmpty() ||
            inspectionDate == null || inspectionDate.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin lịch hẹn.");
            redirectToScheduleInspectionPage(request, response);
            return;
        }

        int vehicleId, stationId;
        try {
            vehicleId = Integer.parseInt(vehicleIdStr);
            stationId = Integer.parseInt(stationIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID phương tiện và trạm kiểm định phải là số hợp lệ.");
            redirectToScheduleInspectionPage(request, response);
            return;
        }

        try {
            // Kiểm tra trạng thái xác minh của phương tiện
            if (!verificationDAO.isVehicleApproved(vehicleId)) {
                session.setAttribute("errorMessage", "Phương tiện chưa được xác minh! Vui lòng liên hệ quản trị viên.");
                redirectToScheduleInspectionPage(request, response);
                return;
            }

            // Kiểm tra xem phương tiện đã có lịch hẹn đang chờ chưa (tùy chọn)
            if (scheduleDAO.hasPendingSchedule(vehicleId)) {
                session.setAttribute("errorMessage", "Phương tiện đã có lịch hẹn đang chờ xử lý.");
                redirectToScheduleInspectionPage(request, response);
                return;
            }

            // Thêm lịch hẹn vào InspectionSchedules
            boolean scheduleAdded = scheduleDAO.addSchedule(vehicleId, stationId, ownerId, inspectionDate);
            if (!scheduleAdded) {
                session.setAttribute("errorMessage", "Không thể tạo lịch hẹn. Vui lòng thử lại.");
                redirectToScheduleInspectionPage(request, response);
                return;
            }

            // Lấy thông tin trạm kiểm định để tạo thông báo
            String stationName = scheduleDAO.getStationName(stationId);
            if (stationName == null) {
                stationName = "Unknown Station";
            }

            // Gửi thông báo xác nhận lịch hẹn
            String message = "Bạn đã lên lịch kiểm định vào " + inspectionDate + " tại cơ sở " + stationName + ".";
            boolean notificationAdded = notificationDAO.addNotification(ownerId, message, "Schedule");
            if (!notificationAdded) {
                session.setAttribute("errorMessage", "Lịch hẹn đã được tạo nhưng không thể gửi thông báo.");
                redirectToScheduleInspectionPage(request, response);
                return;
            }

            // Ghi log
            boolean logAdded = logDAO.addLog(ownerId, "Schedule Inspection");
            if (!logAdded) {
                session.setAttribute("errorMessage", "Lịch hẹn đã được tạo nhưng không thể ghi log.");
                redirectToScheduleInspectionPage(request, response);
                return;
            }

            session.setAttribute("successMessage", "Lịch hẹn kiểm định đã được tạo thành công.");
            redirectToScheduleInspectionPage(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            redirectToScheduleInspectionPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            redirectToScheduleInspectionPage(request, response);
        }
    }

    private void redirectToScheduleInspectionPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/owner/scheduleInspectionPage");
    }
}