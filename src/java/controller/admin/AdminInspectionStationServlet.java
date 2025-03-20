package controller.admin;

import dao.InspectionStationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.InspectionStation;

@WebServlet(name = "AdminInspectionStationServlet", urlPatterns = {"/admin/inspectionStationManagement"})
public class AdminInspectionStationServlet extends HttpServlet {

    private final InspectionStationDAO stationDAO = new InspectionStationDAO();
    private static final int PAGE_SIZE = 10; // Số trạm trên mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Kiểm tra quyền Admin
            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("role");
            if (role == null || !role.equals("Admin")) {
                request.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này!");
                request.getRequestDispatcher("/view/login.jsp").forward(request, response);
                return;
            }

            // Lấy tham số từ request
            String searchKeyword = request.getParameter("searchKeyword");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

            // Lấy danh sách trạm kiểm định
            List<InspectionStation> stations = stationDAO.getStations(searchKeyword, sortBy, sortOrder, page, PAGE_SIZE);

            // Tính tổng số trang
            int totalStations = stationDAO.countStations(searchKeyword);
            int totalPages = (int) Math.ceil((double) totalStations / PAGE_SIZE);

            // Truyền dữ liệu sang JSP
            request.setAttribute("stations", stations);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/view/secure/admin/inspectionStationManagement.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách trạm kiểm định: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/admin/inspectionStationManagement.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if ("viewStats".equals(action)) {
                // Xem thống kê hoạt động trạm
                int stationId = Integer.parseInt(request.getParameter("stationId"));
                Map<String, Object> stats = stationDAO.getStationStatistics(stationId);
                InspectionStation station = stationDAO.getStationById(stationId);

                // Truyền dữ liệu thống kê và thông tin trạm sang JSP
                request.setAttribute("stationStats", stats);
                request.setAttribute("station", station);
            } else if ("add".equals(action)) {
                // Thêm trạm kiểm định mới
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");

                if (stationDAO.isEmailExist(email)) {
                    request.setAttribute("errorMessage", "Email đã tồn tại!");
                } else if (stationDAO.isPhoneExist(phone)) {
                    request.setAttribute("errorMessage", "Số điện thoại đã tồn tại!");
                } else {
                    InspectionStation station = new InspectionStation();
                    station.setName(name);
                    station.setAddress(address);
                    station.setPhone(phone);
                    station.setEmail(email);

                    boolean success = stationDAO.addStation(station);
                    if (success) {
                        request.setAttribute("successMessage", "Thêm trạm kiểm định thành công!");
                    } else {
                        request.setAttribute("errorMessage", "Không thể thêm trạm kiểm định!");
                    }
                }
            } else if ("update".equals(action)) {
                // Cập nhật thông tin trạm kiểm định
                int stationId = Integer.parseInt(request.getParameter("stationId"));
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");

                InspectionStation station = stationDAO.getStationById(stationId);
                station.setName(name);
                station.setAddress(address);
                station.setPhone(phone);
                station.setEmail(email);

                boolean success = stationDAO.updateStation(station);
                if (success) {
                    request.setAttribute("successMessage", "Cập nhật trạm kiểm định thành công!");
                } else {
                    request.setAttribute("errorMessage", "Không thể cập nhật trạm kiểm định!");
                }
            } else if ("delete".equals(action)) {
                // Xóa trạm kiểm định
                int stationId = Integer.parseInt(request.getParameter("stationId"));
                boolean success = stationDAO.deleteStation(stationId);
                if (success) {
                    request.setAttribute("successMessage", "Xóa trạm kiểm định thành công!");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa trạm kiểm định!");
                }
            }

            // Sau khi xử lý, tải lại danh sách trạm kiểm định
            String searchKeyword = request.getParameter("searchKeyword");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

            List<InspectionStation> stations = stationDAO.getStations(searchKeyword, sortBy, sortOrder, page, PAGE_SIZE);
            int totalStations = stationDAO.countStations(searchKeyword);
            int totalPages = (int) Math.ceil((double) totalStations / PAGE_SIZE);

            request.setAttribute("stations", stations);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/view/secure/admin/inspectionStationManagement.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/admin/inspectionStationManagement.jsp").forward(request, response);
        }
    }
}