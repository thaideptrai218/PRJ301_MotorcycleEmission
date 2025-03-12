package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Không tạo session mới nếu chưa có

        // Kiểm tra session
        if (session == null || session.getAttribute("role") == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/signin.jsp");
            return;
        }

        // Lấy role từ session
        String role = (String) session.getAttribute("role");
        String requestURI = httpRequest.getRequestURI();

        // Kiểm tra quyền truy cập dựa trên role và URL
        boolean isAuthorized = false;

        if ("Owner".equals(role) && requestURI.endsWith("/secure/ownerDashboard.jsp")) {
            isAuthorized = true;
        } else if ("Inspector".equals(role) && requestURI.endsWith("/secure/inspectorDashboard.jsp")) {
            isAuthorized = true;
        } else if ("Station".equals(role) && requestURI.endsWith("/secure/stationDashboard.jsp")) {
            isAuthorized = true;
        } else if ("Police".equals(role) && requestURI.endsWith("/secure/policeDashboard.jsp")) {
            isAuthorized = true;
        }

        // Nếu không có quyền, chuyển hướng về trang lỗi hoặc signin
        if (!isAuthorized) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/error.jsp");
            return;
        }

        // Cho phép tiếp tục nếu có quyền
        chain.doFilter(request, response);
    }
}