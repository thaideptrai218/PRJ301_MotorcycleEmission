package controller.admin;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.User;

@WebServlet(name = "AdminUserManagementServlet", urlPatterns = {"/admin/userManagement"})
public class AdminUserManagementServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private static final int PAGE_SIZE = 10; // Số tài khoản trên mỗi trang

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
            String roleFilter = request.getParameter("roleFilter");
            String lockedFilter = request.getParameter("lockedFilter");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

            // Lấy danh sách người dùng
            List<User> users = userDAO.getUsers(searchKeyword, roleFilter, lockedFilter, sortBy, sortOrder, page, PAGE_SIZE);

            // Tính tổng số trang
            int totalUsers = userDAO.countUsers(searchKeyword, roleFilter, lockedFilter);
            int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);

            // Truyền dữ liệu sang JSP
            request.setAttribute("users", users);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("lockedFilter", lockedFilter);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/view/secure/admin/userManagement.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/admin/userManagement.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if ("add".equals(action)) {
                // Thêm người dùng mới
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                String phone = request.getParameter("phone");

                if (userDAO.isEmailExist(email)) {
                    request.setAttribute("errorMessage", "Email đã tồn tại!");
                } else if (userDAO.isPhoneExist(phone)) {
                    request.setAttribute("errorMessage", "Số điện thoại đã tồn tại!");
                } else {
                    User user = new User();
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setPassword(password); // Nên mã hóa mật khẩu trong thực tế
                    user.setRole(role);
                    user.setPhone(phone);
                    user.setLocked(false);

                    boolean success = userDAO.registerUser(user);
                    if (success) {
                        request.setAttribute("successMessage", "Thêm người dùng thành công!");
                    } else {
                        request.setAttribute("errorMessage", "Không thể thêm người dùng!");
                    }
                }
            } else if ("update".equals(action)) {
                // Cập nhật thông tin người dùng
                int userId = Integer.parseInt(request.getParameter("userId"));
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String role = request.getParameter("role");

                User user = userDAO.getUserById(userId);
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(role);

                boolean success = userDAO.updateUser(user);
                if (success) {
                    request.setAttribute("successMessage", "Cập nhật người dùng thành công!");
                } else {
                    request.setAttribute("errorMessage", "Không thể cập nhật người dùng!");
                }
            } else if ("delete".equals(action)) {
                // Xóa người dùng
                int userId = Integer.parseInt(request.getParameter("userId"));
                boolean success = userDAO.deleteUser(userId);
                if (success) {
                    request.setAttribute("successMessage", "Xóa người dùng thành công!");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa người dùng!");
                }
            } else if ("toggleLock".equals(action)) {
                // Khóa/Mở khóa tài khoản
                int userId = Integer.parseInt(request.getParameter("userId"));
                boolean lock = Boolean.parseBoolean(request.getParameter("lock"));
                boolean success = userDAO.toggleLockUser(userId, lock);
                if (success) {
                    request.setAttribute("successMessage", lock ? "Khóa tài khoản thành công!" : "Mở khóa tài khoản thành công!");
                } else {
                    request.setAttribute("errorMessage", "Không thể thay đổi trạng thái tài khoản!");
                }
            }

            // Sau khi xử lý, tải lại danh sách người dùng
            String searchKeyword = request.getParameter("searchKeyword");
            String roleFilter = request.getParameter("roleFilter");
            String lockedFilter = request.getParameter("lockedFilter");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

            List<User> users = userDAO.getUsers(searchKeyword, roleFilter, lockedFilter, sortBy, sortOrder, page, PAGE_SIZE);
            int totalUsers = userDAO.countUsers(searchKeyword, roleFilter, lockedFilter);
            int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);

            request.setAttribute("users", users);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("lockedFilter", lockedFilter);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/view/secure/admin/userManagement.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/admin/userManagement.jsp").forward(request, response);
        }
    }
}