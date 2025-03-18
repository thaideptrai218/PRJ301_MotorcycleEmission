package controller.owner;

import dao.InspectionScheduleDAO;
import dao.NotificationDAO;
import dao.LogDAO;
import dao.RequestDAO;
import dao.VehicleDAO;
import model.InspectionSchedule;
import model.Request;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Vehicle;

public class ScheduleInspectionServlet extends HttpServlet {
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final LogDAO logDAO = new LogDAO();
    private final RequestDAO requestDAO = new RequestDAO();
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

        String vehicleIdStr = request.getParameter("vehicleId");
        String stationIdStr = request.getParameter("stationId");
        String inspectionDateStr = request.getParameter("inspectionDate");

        if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty() ||
            stationIdStr == null || stationIdStr.trim().isEmpty() ||
            inspectionDateStr == null || inspectionDateStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            int stationId = Integer.parseInt(stationIdStr);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date scheduleDate = sdf.parse(inspectionDateStr);

            // Tạo Request
            Request ownerRequest = new Request();
            ownerRequest.setCreatedBy(ownerId);
            ownerRequest.setAssignedTo(stationId);
            ownerRequest.setVehicleID(vehicleId);
            ownerRequest.setType("InspectionSchedule");
            ownerRequest.setMessage("Yêu cầu đặt lịch kiểm tra khí thải xe máy.");
            ownerRequest.setStatus("Pending");
            ownerRequest.setPriority("Medium");

            // Thêm Request và lấy RequestID
            int requestId = requestDAO.addRequest(ownerRequest);

            // Tạo InspectionSchedule
            InspectionSchedule schedule = new InspectionSchedule();
            schedule.setVehicleID(vehicleId);
            schedule.setStationID(stationId);
            schedule.setOwnerID(ownerId);
            schedule.setScheduleDate(scheduleDate);
            schedule.setStatus("Pending");
            schedule.setRequestID(requestId);

            // Thêm InspectionSchedule
            if (!inspectionScheduleDAO.addInspectionSchedule(schedule)) {
                throw new SQLException("Không thể gửi lịch hẹn.");
            }

            Vehicle ownerVehicle = vehicleDAO.getVehicleById(vehicleId);
            // Gửi thông báo
            String ownerMessage = "Lịch hẹn kiểm định cho phương tiện  " + ownerVehicle.getPlateNumber() + " đã được gửi.";
            notificationDAO.addNotification(ownerId, ownerMessage, "Schedule");

            // Ghi log
            logDAO.addLog(ownerId, "Đặt Lịch Kiểm Tra");

            // Chuyển hướng thành công
            session.setAttribute("successMessage", "Lịch hẹn kiểm định đã được gửi thành công. Xem chi tiết tại <a href='" + request.getContextPath() + "/owner/inspectionRequests'>Inspection Requests</a>.");
            response.sendRedirect(request.getContextPath() + "/owner/inspectionRequests");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID phương tiện hoặc trạm không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
        } catch (ParseException e) {
            session.setAttribute("errorMessage", "Ngày kiểm định không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/scheduleInspection");
        }
    }
}