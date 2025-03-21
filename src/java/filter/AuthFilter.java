package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Không tạo session mới nếu chưa có

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Danh sách các trang công khai (không cần đăng nhập)
        boolean isPublicPage = requestURI.endsWith("index.html")
                || requestURI.endsWith("signin.jsp")
                || requestURI.endsWith("register.jsp")
                || requestURI.endsWith("signup.jsp")
                || requestURI.startsWith(contextPath + "/assets/")
                || requestURI.endsWith("register")
                || requestURI.endsWith("login")
                || requestURI.endsWith("logout");
        // Cho phép truy cập tài nguyên tĩnh (CSS, JS, images)

        // Nếu là trang công khai, cho phép truy cập mà không cần kiểm tra đăng nhập
        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // Nếu không phải trang công khai, kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/view/signin.jsp");
            return;
        }

        // Nếu đã đăng nhập, cho phép tiếp tục (sẽ được xử lý bởi RoleFilter nếu cần)
        chain.doFilter(request, response);
    }
}
