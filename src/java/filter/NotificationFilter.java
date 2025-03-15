package filter;

import dao.NotificationDAO;
import model.Notification;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter("/owner/*")
public class NotificationFilter implements Filter {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì đặc biệt
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                try {
                    // Lấy tổng số thông báo chưa đọc
                    int unreadCount = notificationDAO.getUnreadCount(userId);
                    // Lấy tối đa 5 thông báo chưa đọc mới nhất
                    List<Notification> topUnreadNotifications = notificationDAO.getTopUnreadNotifications(userId, 5);
                    // Lưu vào session scope
                    session.setAttribute("unreadCount", unreadCount);
                    session.setAttribute("topUnreadNotifications", topUnreadNotifications);
                } catch (SQLException e) {
                    e.printStackTrace();
                    session.setAttribute("unreadCount", 0);
                    session.setAttribute("topUnreadNotifications", null);
                }
            } else {
                session.setAttribute("unreadCount", 0);
                session.setAttribute("topUnreadNotifications", null);
            }
        }

        // Tiếp tục xử lý request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần dọn dẹp gì đặc biệt
    }
}