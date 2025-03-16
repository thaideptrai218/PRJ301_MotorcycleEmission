package controller.station;

import jakarta.servlet.annotation.WebServlet;
import dao.RequestDAO;
import dao.InspectionScheduleDAO;
import dao.VerificationRecordDAO;
import model.Request;
import model.InspectionSchedule;
import model.VerificationRecord;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "StationRequestsServlet", urlPatterns = {"/station/requests"})
public class StationRequestsServlet extends HttpServlet {

    private final RequestDAO requestDAO = new RequestDAO();
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();
    private final VerificationRecordDAO verificationRecordDAO = new VerificationRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer stationId = (Integer) session.getAttribute("userId");
        try {
            // Lấy tất cả yêu cầu của Station (CreatedBy hoặc AssignedTo)
            List<Request> requests = requestDAO.getRequestsByUserId(stationId);
            // Lọc các yêu cầu trạng thái "Pending"
            List<Request> pendingRequests = requests.stream()
                    .filter(req -> "Pending".equals(req.getStatus()))
                    .collect(Collectors.toList());
            request.setAttribute("requests", pendingRequests);
            request.getRequestDispatcher("/view/secure/station/requests.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải danh sách yêu cầu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/station/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer stationId = (Integer) session.getAttribute("stationId");

        if (stationId == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdStr == null || requestIdStr.trim().isEmpty() || action == null) {
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/station/requests");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID yêu cầu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/station/requests");
            return;
        }

        try {
            Request requestObj = requestDAO.getRequestById(requestId);
            if (requestObj == null) {
                session.setAttribute("errorMessage", "Yêu cầu không tồn tại.");
                response.sendRedirect(request.getContextPath() + "/station/requests");
                return;
            }

            if ("approve".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Processing", stationId);

                // Xử lý theo loại yêu cầu
                if ("InspectionSchedule".equals(requestObj.getType()) && requestObj.getVehicleID() != null) {
                    InspectionSchedule schedule = inspectionScheduleDAO.getScheduleByRequestId(requestId);
                    if (schedule == null) {
                        schedule = new InspectionSchedule();
                        schedule.setVehicleID(requestObj.getVehicleID());
                        schedule.setStationID(stationId);
                        schedule.setOwnerID(requestObj.getCreatedBy());
                        schedule.setScheduleDate(new java.util.Date()); // Có thể lấy từ form hoặc mặc định
                        schedule.setStatus("Confirmed");
                        schedule.setRequestID(requestId); // Liên kết với RequestID
                        inspectionScheduleDAO.addInspectionSchedule(schedule);
                    } else {
                        inspectionScheduleDAO.updateStatus(schedule.getScheduleID(), "Confirmed");
                    }
                } else if ("VehicleVerification".equals(requestObj.getType()) && requestObj.getVehicleID() != null) {
                    VerificationRecord record = verificationRecordDAO.getVerificationRecordByRequestId(requestId);
                    if (record == null) {
                        record = new VerificationRecord();
                        record.setVehicleID(requestObj.getVehicleID());
                        record.setVerifiedBy(stationId); // Giả định Station là người xác minh
                        record.setStatus("Approved");
                        record.setRequestID(requestId); // Liên kết với RequestID
                        verificationRecordDAO.addVerificationRecord(record);
                    } else {
                        verificationRecordDAO.updateStatus(record.getVerificationID(), "Approved");
                    }
                }

                session.setAttribute("successMessage", "Yêu cầu đã được chấp nhận và xử lý.");

            } else if ("reject".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Rejected", null);
                session.setAttribute("successMessage", "Yêu cầu đã bị từ chối.");

                // Cập nhật trạng thái liên quan nếu tồn tại
                InspectionSchedule schedule = inspectionScheduleDAO.getScheduleByRequestId(requestId);
                if (schedule != null) {
                    inspectionScheduleDAO.updateStatus(schedule.getScheduleID(), "Cancelled");
                }
                VerificationRecord record = verificationRecordDAO.getVerificationRecordByRequestId(requestId);
                if (record != null) {
                    verificationRecordDAO.updateStatus(record.getVerificationID(), "Rejected");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/station/requests");
    }
}
