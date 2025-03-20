package controller.police;

import dao.NotificationDAO;
import dao.UserDAO;
import dao.VehicleDAO;
import dao.ViolationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import model.User;
import model.Vehicle;
import model.Violation;

@WebServlet(name = "PoliceRecordViolationServlet", urlPatterns = {"/police/recordViolation"})
public class PoliceRecordViolationServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ViolationDAO violationDAO = new ViolationDAO();
    private final UserDAO userDAO = new UserDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String plateNumber = request.getParameter("plateNumber");

            if (plateNumber == null || plateNumber.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Biển số xe không hợp lệ!");
                request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);
                return;
            }

            // Tìm kiếm xe theo biển số
            Vehicle vehicle = vehicleDAO.getVehicleByPlateNumber(plateNumber);
            if (vehicle == null) {
                request.setAttribute("errorMessage", "Không tìm thấy xe với biển số: " + plateNumber);
                request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin người sở hữu
            User owner = userDAO.getUserById(vehicle.getOwnerID());
            if (owner == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin người sở hữu cho xe này!");
            }

            request.setAttribute("vehicle", vehicle);
            request.setAttribute("owner", owner); // Truyền thông tin người sở hữu
            request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải thông tin xe: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String vehicleIdParam = request.getParameter("vehicleID");
            String reason = request.getParameter("reason");
            String penaltyAmountParam = request.getParameter("penaltyAmount");

            // Kiểm tra dữ liệu đầu vào
            StringBuilder errorMessage = new StringBuilder();
            if (vehicleIdParam == null || vehicleIdParam.trim().isEmpty()) {
                errorMessage.append("Vehicle ID is missing! ");
            }
            if (reason == null || reason.trim().isEmpty()) {
                errorMessage.append("Reason is missing! ");
            }
            if (penaltyAmountParam == null || penaltyAmountParam.trim().isEmpty()) {
                errorMessage.append("Penalty amount is missing! ");
            }

            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString().trim());
                request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);
                return;
            }

            int vehicleID = Integer.parseInt(vehicleIdParam);
            BigDecimal penaltyAmount = new BigDecimal(penaltyAmountParam);

            // Lấy PoliceID từ session
            HttpSession session = request.getSession();
            Integer policeID = (Integer) session.getAttribute("userId");

            // Tạo đối tượng Violation
            Violation violation = new Violation();
            violation.setVehicleID(vehicleID);
            violation.setPoliceID(policeID);
            violation.setReason(reason);
            violation.setPenaltyAmount(penaltyAmount);
            violation.setStatus("Resolved");

            // Thêm vi phạm vào cơ sở dữ liệu
            boolean success = violationDAO.addViolation(violation);
            if (!success) {
                request.setAttribute("errorMessage", "Không thể ghi lỗi vi phạm. Vui lòng thử lại!");
                request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);
                return;
            }

            // Điều hướng lại trang tra cứu với thông báo thành công
            request.getSession().setAttribute("successMessage", "Ghi lỗi vi phạm thành công!");
            
            Vehicle ownerVehicle = vehicleDAO.getVehicleById(vehicleID);
            String ownerMessage = "Phương tiện của bạn (" + ownerVehicle.getPlateNumber() + ") đã bị phạt.";
            success = notificationDAO.addNotification(ownerVehicle.getOwnerID(), ownerMessage, "Violation");
            if (!success) {
                request.setAttribute("errorMessage", "Không thể gửi thông báo cho người dùng. Vui lòng thử lại!");
                request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/police/inspectionLookup?plateNumber=" + request.getParameter("plateNumber"));

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Số tiền phạt không hợp lệ!");
            request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi ghi lỗi vi phạm: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/police/recordViolation.jsp").forward(request, response);
        }
    }
}