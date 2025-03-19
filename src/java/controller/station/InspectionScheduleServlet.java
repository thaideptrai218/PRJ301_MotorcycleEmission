package controller.station;

import dao.InspectionScheduleDAO;
import dao.NotificationDAO;
import dao.RequestDAO;
import dao.UserDAO;
import dao.VehicleDAO;
import model.InspectionSchedule;
import model.Request;
import model.User;
import model.Vehicle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//@WebServlet(name = "InspectionScheduleServlet", urlPatterns = {"/station/inspectionSchedule"})
public class InspectionScheduleServlet extends HttpServlet {

    private final RequestDAO requestDAO = new RequestDAO();
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO();
    private final UserDAO userDAO = new UserDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        Integer inspectionStationId = (Integer) session.getAttribute("inspectionStationId");
        

        // Lấy các tham số từ request
        String statusFilter = request.getParameter("statusFilter");
        String searchKeyword = request.getParameter("searchKeyword");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        int page = 1;
        int pageSize = 10;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1; // Mặc định là trang 1 nếu không có tham số
        }

        try {
            // Lấy danh sách yêu cầu InspectionSchedule
            List<Request> requests = requestDAO.getInspectionScheduleRequests(inspectionStationId, statusFilter, searchKeyword, fromDate, toDate, sortBy, sortOrder, page, pageSize);
            int totalRequests = requestDAO.countInspectionScheduleRequests(inspectionStationId, statusFilter, searchKeyword, fromDate, toDate);
            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);

            // Lấy thông tin chi tiết nếu có requestId
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr != null && !requestIdStr.trim().isEmpty()) {
                int requestId = Integer.parseInt(requestIdStr);
                Request selectedRequest = requestDAO.getRequestById(requestId);
                if (selectedRequest != null) {
                    User user = userDAO.getUserById(selectedRequest.getCreatedBy());
                    InspectionSchedule schedule = inspectionScheduleDAO.getScheduleByRequestId(requestId);
                    if (schedule != null) {
                        Vehicle vehicle = vehicleDAO.getVehicleById(schedule.getVehicleID());
                        request.setAttribute("selectedRequest", selectedRequest);
                        request.setAttribute("user", user);
                        request.setAttribute("vehicle", vehicle);
                        request.setAttribute("schedule", schedule);
                    }
                }
            }

            // Thiết lập các thuộc tính cho JSP
            request.setAttribute("requests", requests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);

            request.getRequestDispatcher("/view/secure/station/inspectionSchedule.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi lấy danh sách yêu cầu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/station/inspectionSchedule");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdStr == null || action == null) {
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/station/inspectionSchedule");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdStr);
            Request requestObj = requestDAO.getRequestById(requestId);
            if (requestObj == null || !"InspectionSchedule".equals(requestObj.getType())) {
                session.setAttribute("errorMessage", "Yêu cầu không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/station/inspectionSchedule");
                return;
            }

            // Xử lý hành động
            if ("approve".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Completed");
                inspectionScheduleDAO.updateStatusByRequestId(requestId, "Confirmed");
                session.setAttribute("successMessage", "Yêu cầu đã được chấp thuận.");
                
                Vehicle ownerVehicle = vehicleDAO.getVehicleById(requestObj.getVehicleID());
                String ownerMessage = "Phương tiện của bạn (" + ownerVehicle.getPlateNumber() + ") đã được chấp nhận kiểm tra";
                notificationDAO.addNotification(requestObj.getCreatedBy() , ownerMessage, "Result");
            } else if ("reject".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Rejected");
                inspectionScheduleDAO.updateStatusByRequestId(requestId, "Cancelled");
                session.setAttribute("successMessage", "Yêu cầu đã bị từ chối.");
                
                Vehicle ownerVehicle = vehicleDAO.getVehicleById(requestObj.getVehicleID());
                String ownerMessage = "Phương tiện của bạn (" + ownerVehicle.getPlateNumber() + ") đã bị từ chối kiểm tra";
                notificationDAO.addNotification(requestObj.getCreatedBy() , ownerMessage, "Result");
            }

            response.sendRedirect(request.getContextPath() + "/station/inspectionSchedule");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/station/inspectionSchedule");
        }
    }
}