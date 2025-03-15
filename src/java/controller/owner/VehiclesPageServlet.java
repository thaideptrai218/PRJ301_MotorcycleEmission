package controller.owner;

import dao.VehicleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Vehicle;

public class VehiclesPageServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("userId");

        try {
            // Lấy danh sách phương tiện cùng trạng thái xác minh
            List<Vehicle> vehicles = vehicleDAO.getVehiclesByOwnerIdWithVerificationStatus(ownerId);
            request.setAttribute("vehicles", vehicles);
            request.getRequestDispatcher("/view/secure/owner/vehicles.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải danh sách phương tiện: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/vehiclesPage");
        }
    }
}
