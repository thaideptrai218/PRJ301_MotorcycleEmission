package controller.owner;

import dao.VehicleDAO;
import dao.VerificationRecordDAO;
import dao.NotificationDAO;
import dao.LogDAO;
import dao.RequestDAO;
import model.Vehicle;
import model.VerificationRecord;
import model.Request;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddVehicleServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VerificationRecordDAO verificationRecordDAO = new VerificationRecordDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final LogDAO logDAO = new LogDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        String plateNumber = request.getParameter("plateNumber");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String manufactureYearStr = request.getParameter("manufactureYear");
        String engineNumber = request.getParameter("engineNumber");

        if (plateNumber == null || plateNumber.trim().isEmpty()
                || brand == null || brand.trim().isEmpty()
                || model == null || model.trim().isEmpty()
                || manufactureYearStr == null || manufactureYearStr.trim().isEmpty()
                || engineNumber == null || engineNumber.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin.");
            response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");
            return;
        }

        try {
            int manufactureYear = Integer.parseInt(manufactureYearStr);

            // Tạo và thêm Vehicle
            Vehicle vehicle = new Vehicle();
            vehicle.setOwnerID(ownerId);
            vehicle.setPlateNumber(plateNumber);
            vehicle.setBrand(brand);
            vehicle.setModel(model);
            vehicle.setManufactureYear(manufactureYear);
            vehicle.setEngineNumber(engineNumber);
            int vehicleId = vehicleDAO.addVehicle(vehicle);

            if (vehicleId == -1) {
                throw new SQLException("Không thể thêm phương tiện.");
            }

            // Tạo VerificationRecord
            VerificationRecord verificationRecord = new VerificationRecord();
            verificationRecord.setVehicleID(vehicleId);
            verificationRecord.setStatus("Pending");

            // Tạo Request
            Request ownerRequest = new Request();
            ownerRequest.setCreatedBy(ownerId);
            ownerRequest.setAssignedTo(null); // Chưa gán Station
            ownerRequest.setVehicleID(vehicleId);
            ownerRequest.setType("VehicleVerification");
            ownerRequest.setMessage("Yêu cầu xác thực xe máy với biển số " + plateNumber + ".");
            ownerRequest.setStatus("Pending");
            ownerRequest.setPriority("High");
            int requestId = requestDAO.addRequest(ownerRequest);

            // Cập nhật VerificationRecord với RequestID (nếu cần)
            verificationRecord.setRequestID(requestId);
            verificationRecordDAO.addVerificationRecord(verificationRecord);

            // Gửi thông báo
            String ownerMessage = "Phương tiện của bạn (" + plateNumber + ") đã được thêm và đang chờ xác minh.";
            notificationDAO.addNotification(ownerId, ownerMessage, "Verify");

            // Ghi log
            logDAO.addLog(ownerId, "Thêm Phương Tiện");

            // Chuyển hướng thành công
            session.setAttribute("successMessage", "Phương tiện đã được thêm thành công và đang chờ xác minh.");
            response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Năm sản xuất phải là một số hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");
        }
    }
}
