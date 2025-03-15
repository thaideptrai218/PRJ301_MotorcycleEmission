package controller.owner;

import dao.NotificationDAO;
import model.Notification;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NotificationsPageServlet extends HttpServlet {
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private static final int PAGE_SIZE = 5; // Số mục trên mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Xử lý phân trang
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            // Lấy danh sách thông báo và tổng số
            List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId, page, PAGE_SIZE);
            int totalNotifications = notificationDAO.getTotalNotifications(userId);
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);

            request.setAttribute("notifications", notifications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/view/secure/owner/notifications.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi tải danh sách thông báo: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/notificationsPage");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        int notificationId = Integer.parseInt(request.getParameter("notificationId"));

        try {
            if ("markAsRead".equals(action)) {
                if (notificationDAO.markAsRead(notificationId, true)) { // Đánh dấu là đã đọc (IsRead = 1)
                    session.setAttribute("successMessage", "Thông báo đã được đánh dấu là đã đọc.");
                } else {
                    session.setAttribute("errorMessage", "Không thể đánh dấu thông báo là đã đọc.");
                }
            } else if ("delete".equals(action)) {
                if (notificationDAO.deleteNotification(notificationId)) {
                    session.setAttribute("successMessage", "Thông báo đã được xóa.");
                } else {
                    session.setAttribute("errorMessage", "Không thể xóa thông báo.");
                }
            }
            response.sendRedirect(request.getContextPath() + "/owner/notificationsPage?page=" + request.getParameter("page"));
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/notificationsPage?page=" + request.getParameter("page"));
        }
    }
}