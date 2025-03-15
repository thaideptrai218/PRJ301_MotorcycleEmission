package controller.owner;

import dao.VehicleDAO;
import dao.VerificationDAO;
import dao.NotificationDAO;
import dao.LogDAO;
import dao.RequestDAO;
import model.Vehicle;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Request;

public class AddVehicleServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VerificationDAO verificationDAO = new VerificationDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final LogDAO logDAO = new LogDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        // Lấy dữ liệu từ form
        String plateNumber = request.getParameter("plateNumber");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String manufactureYearStr = request.getParameter("manufactureYear");
        String engineNumber = request.getParameter("engineNumber");

        // Xác thực dữ liệu đầu vào
        if (plateNumber == null || plateNumber.trim().isEmpty()
                || brand == null || brand.trim().isEmpty()
                || model == null || model.trim().isEmpty()
                || manufactureYearStr == null || manufactureYearStr.trim().isEmpty()
                || engineNumber == null || engineNumber.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin phương tiện.");
            redirectToAddVehiclePage(request, response);
            return;
        }

        int manufactureYear;
        try {
            manufactureYear = Integer.parseInt(manufactureYearStr);
            if (manufactureYear < 1900 || manufactureYear > 9999) {
                session.setAttribute("errorMessage", "Năm sản xuất không hợp lệ.");
                redirectToAddVehiclePage(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Năm sản xuất phải là một số hợp lệ.");
            redirectToAddVehiclePage(request, response);
            return;
        }

        // Tạo đối tượng Vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setOwnerID(ownerId);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setManufactureYear(manufactureYear);
        vehicle.setEngineNumber(engineNumber);

        try {
            // Thêm phương tiện vào cơ sở dữ liệu
            int vehicleId = vehicleDAO.addVehicle(vehicle);
            if (vehicleId == -1) {
                session.setAttribute("errorMessage", "Không thể thêm phương tiện. Vui lòng thử lại.");
                redirectToAddVehiclePage(request, response);
                return;
            }

            // Thêm bản ghi xác minh
            boolean verificationAdded = verificationDAO.addVerification(vehicleId);
            if (!verificationAdded) {
                session.setAttribute("errorMessage", "Thêm phương tiện thành công nhưng không thể tạo yêu cầu xác minh.");
                redirectToAddVehiclePage(request, response);
                return;
            }

            // Ghi log
            boolean logAdded = logDAO.addLog(ownerId, "Add Vehicle");
            if (!logAdded) {
                session.setAttribute("errorMessage", "Thêm phương tiện thành công nhưng không thể ghi log.");
                redirectToAddVehiclePage(request, response);
                return;
            }

            // Gửi thông báo
            String message = "Phương tiện của bạn (" + plateNumber + ") đã được thêm và đang chờ xác minh.";
            boolean notificationAdded = notificationDAO.addNotification(ownerId, message, "Result");
            if (!notificationAdded) {
                session.setAttribute("errorMessage", "Thêm phương tiện thành công nhưng không thể gửi thông báo.");
                redirectToAddVehiclePage(request, response);
                return;
            }

            Request ownerRequest = new Request();
            ownerRequest.setCreatedBy(ownerId);
            ownerRequest.setVehicleID(vehicleId);
            ownerRequest.setType("VehicleVerification"); // Sử dụng giá trị hợp lệ theo ràng buộc CHECK
            ownerRequest.setMessage("Yêu cầu xác thực xe máy."); // Gán giá trị cho Message (bắt buộc)
            ownerRequest.setStatus("Pending");
            ownerRequest.setPriority("High");

            try {
                requestDAO.addRequest(ownerRequest);
                session.setAttribute("successMessage", "Yêu cầu đã được gửi thành công.");
            } catch (SQLException e) {
                // Ghi log chi tiết lỗi để dễ debug
                e.printStackTrace();
                session.setAttribute("errorMessage", "Lỗi khi gửi yêu cầu: " + e.getMessage());
            }

            // Cập nhật danh sách xe trong session
            session.setAttribute("vehicles", vehicleDAO.getVehiclesByOwnerId(ownerId));
            session.setAttribute("successMessage", "Phương tiện đã được thêm thành công và đang chờ xác minh.");
            redirectToAddVehiclePage(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            redirectToAddVehiclePage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            redirectToAddVehiclePage(request, response);
        }
    }

    private void redirectToAddVehiclePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/owner/addVehiclePage");
    }
}
