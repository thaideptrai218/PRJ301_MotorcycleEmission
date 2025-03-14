package controller.owner;

import dao.InspectionScheduleDAO;
import dao.NotificationDAO;
import dao.VehicleDAO;
import model.InspectionSchedule;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;

public class ScheduleInspectionServlet extends HttpServlet {
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        if (ownerId == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy dữ liệu từ form
        String vehicleIdStr = request.getParameter("vehicleId");
        String stationIdStr = request.getParameter("stationId");
        String inspectionDateStr = request.getParameter("inspectionDate"); // Ví dụ: yyyy-MM-dd

        if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty() ||
            stationIdStr == null || stationIdStr.trim().isEmpty() ||
            inspectionDateStr == null || inspectionDateStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
            return;
        }

        int vehicleId, stationId;
        try {
            vehicleId = Integer.parseInt(vehicleIdStr);
            stationId = Integer.parseInt(stationIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID phương tiện hoặc trạm không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
            return;
        }

        // Kiểm tra quyền sở hữu phương tiện

        // Chuyển đổi ngày từ String sang Date
        Date scheduleDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            scheduleDate = sdf.parse(inspectionDateStr);
        } catch (ParseException e) {
            session.setAttribute("errorMessage", "Ngày kiểm định không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
            return;
        }

        try {
            // Tạo InspectionSchedule
            InspectionSchedule inspectionSchedule = new InspectionSchedule();
            inspectionSchedule.setVehicleID(vehicleId);
            inspectionSchedule.setStationID(stationId);
            inspectionSchedule.setOwnerID(ownerId);
            inspectionSchedule.setScheduleDate(scheduleDate);
            inspectionSchedule.setStatus("Pending");
            boolean scheduleAdded = inspectionScheduleDAO.addInspectionSchedule(inspectionSchedule);

            if (scheduleAdded) {
                // Gửi thông báo cho Owner
                String ownerMessage = "Lịch hẹn kiểm định cho phương tiện ID " + vehicleId + " đã được gửi.";
                notificationDAO.addNotification(ownerId, ownerMessage, "Schedule");

                // Redirect đến trang Inspection Schedules
                session.setAttribute("successMessage", "Lịch hẹn kiểm định đã được gửi thành công. Xem chi tiết tại <a href='${pageContext.request.contextPath}/owner/inspectionRequests'>Inspection Requests</a>.");
                response.sendRedirect(request.getContextPath() + "/owner/inspectionRequests");
            } else {
                session.setAttribute("errorMessage", "Không thể gửi lịch hẹn. Vui lòng thử lại.");
                response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
            }
        } catch (SQLException e) {
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
        }
    }
}