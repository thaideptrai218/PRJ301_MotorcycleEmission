/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.station;

import jakarta.servlet.annotation.WebServlet;
import dao.RequestDAO;
import dao.InspectionScheduleDAO; // Giả định có DAO này
import model.Request;
import model.InspectionSchedule;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "StationRequestsServlet", urlPatterns = {"/StationRequestsServlet"})

public class StationRequestsServlet extends HttpServlet {

    private final RequestDAO requestDAO = new RequestDAO();
    private final InspectionScheduleDAO inspectionScheduleDAO = new InspectionScheduleDAO(); // Giả định

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer stationId = (Integer) session.getAttribute("stationId");
        try {
            List<Request> requests = requestDAO.getPendingRequestsByUserId(stationId);
            request.setAttribute("requests", requests);
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

        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");
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
            if ("approve".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Processing", stationId);
                // Nếu là yêu cầu InspectionSchedule, tạo lịch hẹn
                if ("InspectionSchedule".equals(requestObj.getType()) && requestObj.getVehicleID() != null) {
                    InspectionSchedule schedule = new InspectionSchedule();
                    schedule.setVehicleID(requestObj.getVehicleID());
                    schedule.setStationID(stationId);
                    schedule.setOwnerID(requestObj.getCreatedBy());
                    schedule.setStatus("Confirmed");
                    // Giả định có logic lấy ngày hiện tại hoặc từ request
                    schedule.setScheduleDate(new java.util.Date());
                    inspectionScheduleDAO.addInspectionSchedule(schedule);
                }
                session.setAttribute("successMessage", "Yêu cầu đã được chấp nhận và xử lý.");
            } else if ("reject".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Rejected", null);
                session.setAttribute("successMessage", "Yêu cầu đã bị từ chối.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/station/requests");
    }
}
