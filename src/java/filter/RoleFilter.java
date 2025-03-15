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
        String contextPath = httpRequest.getContextPath();

        // Loại bỏ contextPath để kiểm tra phần còn lại của URL
        String relativeURI = requestURI.substring(contextPath.length());

        // Kiểm tra quyền truy cập dựa trên role và URL
        boolean isAuthorized = false;

        if ("Owner".equals(role)) {
            isAuthorized = relativeURI.startsWith("/owner/") || relativeURI.contains("/view/secure/owner/");
        } else if ("Inspector".equals(role)) {
            isAuthorized = relativeURI.startsWith("/inspector/") || relativeURI.contains("/view/secure/inspector/");
        } else if ("Station".equals(role)) {
            isAuthorized = relativeURI.startsWith("/station/") || relativeURI.contains("/view/secure/station/");
        } else if ("Police".equals(role)) {
            isAuthorized = relativeURI.startsWith("/police/") || relativeURI.contains("/view/secure/police/");
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