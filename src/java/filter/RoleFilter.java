package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoleFilter implements Filter {

    private static final Map<String, Set<String>> rolePermissions = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        // Define allowed paths for each role
        rolePermissions.put("Owner", Set.of("/owner/", "/view/secure/owner/"));
        rolePermissions.put("Inspector", Set.of("/inspector/", "/view/secure/inspector/"));
        rolePermissions.put("Station", Set.of("/station/", "/view/secure/station/"));
        rolePermissions.put("Police", Set.of("/police/", "/view/secure/police/"));
        rolePermissions.put("Admin", Set.of("/admin/", "/view/secure/admin/"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // If no session or role is found, redirect to login page
        if (session == null || session.getAttribute("role") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/signin.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativeURI = requestURI.substring(contextPath.length());

        // Check if the role is allowed to access the requested resource
        if (!isAuthorized(role, relativeURI)) {
            request.setAttribute("loginError", "Vui lòng đăng nhập với quyền phù hợp");
            request.getRequestDispatcher("/view/signin.jsp").forward(request, response);
            return;
        }

        // User is authorized, continue request processing
        chain.doFilter(request, response);
    }

    private boolean isAuthorized(String role, String relativeURI) {
        Set<String> allowedPaths = rolePermissions.get(role);
        if (allowedPaths == null) return false;

        for (String allowedPath : allowedPaths) {
            if (relativeURI.startsWith(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Cleanup resources if needed
    }
}
