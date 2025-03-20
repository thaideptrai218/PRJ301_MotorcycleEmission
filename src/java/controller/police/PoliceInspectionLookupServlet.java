package controller.police;

import dao.InspectionRecordDAO;
import dao.UserDAO;
import dao.VehicleDAO;
import dao.ViolationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.InspectionRecord;
import model.User;
import model.Vehicle;
import model.Violation;

@WebServlet(name = "PoliceInspectionLookupServlet", urlPatterns = {"/police/inspectionLookup"})
public class PoliceInspectionLookupServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final InspectionRecordDAO inspectionRecordDAO = new InspectionRecordDAO();
    private final ViolationDAO violationDAO = new ViolationDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị trang tra cứu
        String plateNumber = request.getParameter("plateNumber");
        if (plateNumber == null) {
            request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);
        } else {
            doPost(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String plateNumber = request.getParameter("plateNumber");

            if (plateNumber == null || plateNumber.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng nhập biển số xe!");
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

            // Lấy thông tin người sở hữu dựa trên OwnerID
            User owner = userDAO.getUserById(vehicle.getOwnerID());
            if (owner == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin người sở hữu cho xe này!");
            }

            // Lấy danh sách kiểm định của xe
            List<InspectionRecord> inspectionRecords = inspectionRecordDAO.getInspectionRecordsByVehicleId(vehicle.getVehicleID());
            if (inspectionRecords.isEmpty()) {
                request.setAttribute("errorMessage", "Xe với biển số " + plateNumber + " chưa có lịch sử kiểm định khí thải.");
            }

            // Lấy danh sách vi phạm của xe
            List<Violation> violations = violationDAO.getViolationsByVehicleId(vehicle.getVehicleID());
            request.setAttribute("plateNumber", plateNumber);
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("owner", owner); // Truyền thông tin người sở hữu
            request.setAttribute("inspectionRecords", inspectionRecords);
            request.setAttribute("violations", violations);
            request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tra cứu thông tin: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/police/inspectionLookup.jsp").forward(request, response);
        }
    }
}
