package controller.station;

import dao.RequestDAO;
import dao.UserDAO;
import dao.VehicleDAO;
import dao.VerificationRecordDAO;
import model.Request;
import model.User;
import model.Vehicle;
import model.VerificationRecord;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "VerifyVehicleServlet", urlPatterns = {"/station/verifyVehicle"})
public class VerifyVehicleServlet extends HttpServlet {

    private final RequestDAO requestDAO = new RequestDAO();
    private final UserDAO userDAO = new UserDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VerificationRecordDAO verificationRecordDAO = new VerificationRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");


        // Lấy các tham số bộ lọc từ request
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
            page = 1;
        }

        // Lấy yêu cầu chi tiết nếu có requestId
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr != null && !requestIdStr.trim().isEmpty()) {
            try {
                int requestId = Integer.parseInt(requestIdStr);
                Request selectedRequest = requestDAO.getRequestById(requestId);
                if (selectedRequest != null && "VehicleVerification".equals(selectedRequest.getType())) {
                    User user = userDAO.getUserById(selectedRequest.getCreatedBy());
                    Vehicle vehicle = vehicleDAO.getVehicleById(selectedRequest.getVehicleID());
                    request.setAttribute("selectedRequest", selectedRequest);
                    request.setAttribute("user", user);
                    request.setAttribute("vehicle", vehicle);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", "Lỗi khi lấy thông tin yêu cầu: " + e.getMessage());
            }
        }

        try {
            // Mặc định hiển thị yêu cầu Pending nếu không có bộ lọc
            if (statusFilter == null || statusFilter.isEmpty()) {
                statusFilter = "";
            }

            // Lấy danh sách yêu cầu VehicleVerification với bộ lọc
            List<Request> vehicleVerificationRequests = requestDAO.getVehicleVerificationRequests(
                    statusFilter, searchKeyword, fromDate, toDate, sortBy, sortOrder, page, pageSize);

            // Đếm tổng số yêu cầu để hỗ trợ phân trang
            int totalRequests = requestDAO.countVehicleVerificationRequests(statusFilter, searchKeyword, fromDate, toDate);
            int totalPages = (int) Math.ceil((double) totalRequests / pageSize);

            request.setAttribute("requests", vehicleVerificationRequests);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);

            request.getRequestDispatcher("/view/secure/station/verifyVehicle.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải danh sách yêu cầu xác minh xe: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/station/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");

        if (requestIdStr == null || requestIdStr.trim().isEmpty() || action == null) {
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/station/verifyVehicle");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID yêu cầu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/station/verifyVehicle");
            return;
        }

        try {
            Request requestObj = requestDAO.getRequestById(requestId);
            if (requestObj == null || !"VehicleVerification".equals(requestObj.getType())) {
                session.setAttribute("errorMessage", "Yêu cầu không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/station/verifyVehicle");
                return;
            }

            if ("approve".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Completed", null);

                // Cập nhật hoặc tạo VerificationRecord
                VerificationRecord record = verificationRecordDAO.getVerificationRecordByRequestId(requestId);
                if (record == null) {
                    record = new VerificationRecord();
                    record.setVehicleID(requestObj.getVehicleID());
                    record.setVerifiedBy(userId);
                    record.setStatus("Approved");
                    record.setRequestID(requestId);
                    verificationRecordDAO.addVerificationRecord(record);
                } else {
                    record.setVerifiedBy(userId);
                    record.setStatus("Approved");
                    verificationRecordDAO.updateStatus(record.getVerificationID(), "Approved");
                }

                session.setAttribute("successMessage", "Yêu cầu xác minh xe đã được chấp thuận.");

            } else if ("reject".equals(action)) {
                requestDAO.updateRequestStatus(requestId, "Rejected", null);

                // Cập nhật VerificationRecord nếu có
                VerificationRecord record = verificationRecordDAO.getVerificationRecordByRequestId(requestId);
                if (record != null) {
                    verificationRecordDAO.updateStatus(record.getVerificationID(), "Rejected");
                }

                session.setAttribute("successMessage", "Yêu cầu xác minh xe đã bị từ chối.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/station/verifyVehicle");
    }
}