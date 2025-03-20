package controller.owner;

import dao.ViolationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Violation;

@WebServlet(name = "OwnerViolationLookupServlet", urlPatterns = {"/owner/violationLookup"})
public class OwnerViolationLookupServlet extends HttpServlet {

    private final ViolationDAO violationDAO = new ViolationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy OwnerID từ session
            HttpSession session = request.getSession();
            Integer ownerId = (Integer) session.getAttribute("userId");
            if (ownerId == null) {
                request.setAttribute("errorMessage", "Bạn cần đăng nhập để xem danh sách vi phạm!");
                request.getRequestDispatcher("/view/login.jsp").forward(request, response);
                return;
            }

            // Lấy tham số bộ lọc và sắp xếp
            String statusFilter = request.getParameter("statusFilter");
            String sortOrder = request.getParameter("sortOrder");

            // Lấy danh sách vi phạm
            List<Violation> violations = violationDAO.getViolationsByOwnerId(ownerId, statusFilter, sortOrder);

            // Truyền dữ liệu sang JSP
            request.setAttribute("violations", violations);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("sortOrder", sortOrder);
            request.getRequestDispatcher("/view/secure/owner/violationLookup.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tra cứu vi phạm: " + e.getMessage());
            request.getRequestDispatcher("/view/secure/owner/violationLookup.jsp").forward(request, response);
        }
    }
}