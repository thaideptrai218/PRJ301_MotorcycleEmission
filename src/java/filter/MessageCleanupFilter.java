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

public class MessageCleanupFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Lấy session nếu có, không tạo mới

        if (session != null) {
            // Kiểm tra xem thông báo đã được hiển thị chưa (dựa trên request attribute tạm thời)
            String errorDisplayed = (String) request.getAttribute("errorMessageDisplayed");
            String successDisplayed = (String) request.getAttribute("successMessageDisplayed");

            if (errorDisplayed == null && session.getAttribute("errorMessage") != null) {
                // Chưa hiển thị, đánh dấu để JSP biết
                request.setAttribute("errorMessageDisplayed", "false");
            }
            if (successDisplayed == null && session.getAttribute("successMessage") != null) {
                // Chưa hiển thị, đánh dấu để JSP biết
                request.setAttribute("successMessageDisplayed", "false");
            }

            // Tiếp tục chain
            chain.doFilter(request, response);

            // Sau khi JSP được render, kiểm tra và xóa nếu đã hiển thị
            if ("true".equals(request.getAttribute("errorMessageDisplayed"))) {
                session.removeAttribute("errorMessage");
            }
            if ("true".equals(request.getAttribute("successMessageDisplayed"))) {
                session.removeAttribute("successMessage");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
