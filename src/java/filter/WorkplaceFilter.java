package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class WorkplaceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        if (session == null || session.getAttribute("role") == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/signin.jsp");
            return;
        }

        if (path.equals("/station/chooseWorkplace") || path.equals("/login") || path.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra vai trò Station hoặc Inspector
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId != null && (role != null && (role.equals("Station") || role.equals("Inspector")))) {
            Integer inspectionStationId = (Integer) session.getAttribute("inspectionStationId");
            if (inspectionStationId == null) {
                session.setAttribute("errorMessage", "Vui lòng chọn nơi làm việc trước.");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/station/chooseWorkplace");
                return;
            }
        } else if (session == null || session.getAttribute("userId") == null) {
            // Chưa đăng nhập, chuyển hướng về signin.jsp
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/signin.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
