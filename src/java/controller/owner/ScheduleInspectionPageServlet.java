package controller.owner;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleInspectionPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Filter đã kiểm tra đăng nhập, chỉ cần điều hướng
        request.getRequestDispatcher("/view/secure/owner/scheduleInspection.jsp").forward(request, response);
    }
}
