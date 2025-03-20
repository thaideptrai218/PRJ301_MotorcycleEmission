package controller.inspector;

import dao.InspectionRecordDAO;
import dao.InspectionScheduleDAO;
import dao.InspectionStationDAO;
import dao.VehicleDAO;
import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.InspectionRecord;
import model.InspectionSchedule;
import model.Vehicle;

@WebServlet(name = "InspectorScheduleServlet", urlPatterns = {"/inspector/inspectionSchedule"})
public class InspectorScheduleServlet extends HttpServlet {

    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final InspectionRecordDAO inspectionRecordDAO = new InspectionRecordDAO();
    private final InspectionStationDAO inspectionStationDAO = new InspectionStationDAO();
    private final InspectionScheduleDAO scheduleDao = new InspectionScheduleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String vehicleIDParam = request.getParameter("vehicleID");
            String stationIDParam = request.getParameter("stationID");
            String reinspect = request.getParameter("reinspect");
            if (vehicleIDParam == null || vehicleIDParam.isEmpty()) {
                request.setAttribute("errorMessage", "Vehicle ID is missing!");
                request.getRequestDispatcher("/view/secure/inspector/schedules.jsp").forward(request, response);
                return;
            }

            int vehicleID = Integer.parseInt(vehicleIDParam);
            int stationID = Integer.parseInt(stationIDParam);
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleID);
            List<InspectionSchedule> schedule = inspectionScheduleDAO.getConfirmedSchedulesByStationId(stationID);

            if (vehicle == null || schedule == null) {
                request.setAttribute("errorMessage", "Vehicle or schedule not found!");
            } else {
                request.setAttribute("vehicle", vehicle);
                request.setAttribute("schedule", schedule);
                request.setAttribute("stationID", stationIDParam);
            }
            
            request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Vehicle ID!");
            request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(InspectorScheduleServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/inspector/scheduleFormPage");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Bước 1: Parse dữ liệu từ request
            String vehicleIDParam = request.getParameter("vehicleID");
            String result = request.getParameter("result");
            String co2EmissionParam = request.getParameter("co2Emission");
            String hcEmissionParam = request.getParameter("hcEmission");
            String comments = request.getParameter("comments");
            String expirationDateParam = request.getParameter("expirationDate");
            String stationIDParam = request.getParameter("stationID");

            // Kiểm tra các trường bắt buộc
            StringBuilder errorMessage = new StringBuilder();
            if (vehicleIDParam == null || vehicleIDParam.isEmpty()) {
                errorMessage.append("Vehicle ID is missing! ");
            }
            if (result == null || result.isEmpty()) {
                errorMessage.append("Result is missing! ");
            }
            if (expirationDateParam == null || expirationDateParam.isEmpty()) {
                errorMessage.append("Expiration date is missing! ");
            }
            if (stationIDParam == null || stationIDParam.isEmpty()) {
                errorMessage.append("Station ID is missing! ");
            }
            if (co2EmissionParam == null || co2EmissionParam.isEmpty()) {
                errorMessage.append("CO2 Emission is missing! ");
            }
            if (hcEmissionParam == null || hcEmissionParam.isEmpty()) {
                errorMessage.append("HC Emission is missing! ");
            }

            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString().trim());
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Parse các giá trị số
            int vehicleID = parseInteger(vehicleIDParam);
            int stationID = parseInteger(stationIDParam);
            double co2Emission = parseDouble(co2EmissionParam);
            double hcEmission = parseDouble(hcEmissionParam);

            if (vehicleID == -1) {
                request.setAttribute("errorMessage", "Invalid Vehicle ID!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }
            if (stationID == -1) {
                request.setAttribute("errorMessage", "Invalid Station ID!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Kiểm tra StationID có tồn tại không
            if (inspectionStationDAO.getStationById(stationID) == null) {
                request.setAttribute("errorMessage", "Station ID " + stationID + " does not exist in InspectionStations!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Bước 2: Parse ngày
            LocalDate expirationDate;
            try {
                expirationDate = LocalDate.parse(expirationDateParam);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Invalid expiration date format: " + expirationDateParam);
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Bước 3: Kiểm tra logic
            if (!result.equals("Pass") && !result.equals("Decline")) {
                request.setAttribute("errorMessage", "Invalid result! Must be 'Pass' or 'Decline'.");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Bước 4: Lấy dữ liệu từ DAO
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleID);
            InspectionSchedule schedule = inspectionScheduleDAO.getScheduleByVehicleId(vehicleID);
            if (vehicle == null || schedule == null) {
                request.setAttribute("errorMessage", "Vehicle or schedule not found!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Bước 5: Xử lý record
            InspectionRecord record = new InspectionRecord();
            record.setVehicleID(vehicleID);
            record.setStationID(stationID);
            Integer inspectorID = (Integer) request.getSession().getAttribute("userId");
            if (inspectorID == null) {
                request.setAttribute("errorMessage", "Inspector ID not found in session!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }
            record.setInspectorID(inspectorID);
            record.setInspectionDate(new java.util.Date()); // Sử dụng Date hiện tại thay vì LocalDateTime
            record.setResult(result);
            record.setCo2Emission(BigDecimal.valueOf(co2Emission));
            record.setHcEmission(BigDecimal.valueOf(hcEmission));
            record.setComments(comments != null ? comments : "");
            // Sửa lỗi Date: Chuyển LocalDate sang java.util.Date
            record.setExpirationDate(java.util.Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            record.setStatus("Completed");

            // Bước 6: Thêm record
            try {
                boolean isInserted = inspectionRecordDAO.addInspectionRecord(record);
                if (!isInserted) {
                    request.setAttribute("errorMessage", "Failed to add inspection record to database!");
                    request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                Logger.getLogger(InspectorScheduleServlet.class.getName()).log(Level.SEVERE, "Database error", e);
                request.setAttribute("errorMessage", "Database error: " + e.getMessage());
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Bước 7: Cập nhật trạng thái lịch hẹn
            boolean isUpdated = inspectionScheduleDAO.updateScheduleStatusToCompleted(schedule.getScheduleID());
            if (!isUpdated) {
                request.setAttribute("errorMessage", "Failed to update schedule status!");
                request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
                return;
            }

            // Thành công
            request.getSession().setAttribute("successMessage", "Inspection record added successfully!");
             HttpSession session = request.getSession(false);
            Integer inspectionStationId = (Integer) session.getAttribute("inspectionStationId");
            List<InspectionSchedule> schedules = scheduleDao.getConfirmedSchedulesByStationId(inspectionStationId);
            request.setAttribute("schedules", schedules);
            request.setAttribute("statusFilter", "Confirmed");
            
            request.getRequestDispatcher("/view/secure/inspector/schedules.jsp").forward(request, response);

        } catch (Exception e) {
            Logger.getLogger(InspectorScheduleServlet.class.getName()).log(Level.SEVERE, "Unexpected error", e);
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/inspector/vehicleInspection.jsp").forward(request, response);
        }

    }

    private int parseInteger(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double parseDouble(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
