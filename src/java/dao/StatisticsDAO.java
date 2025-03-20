package dao;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsDAO {

    private final Connection conn;

    public StatisticsDAO() {
        this.conn = new DBConnection().getConnection();
    }

    // Đếm tổng số người dùng
    public int countUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số lượng người dùng theo vai trò
    public Map<String, Integer> countUsersByRole() throws SQLException {
        Map<String, Integer> usersByRole = new HashMap<>();
        String sql = "SELECT Role, COUNT(*) AS Count FROM Users GROUP BY Role";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String role = rs.getString("Role");
                int count = rs.getInt("Count");
                usersByRole.put(role, count);
            }
        }
        // Đảm bảo có dữ liệu cho tất cả các vai trò
        String[] roles = {"Owner", "Inspector", "Station", "Police", "Admin"};
        for (String role : roles) {
            usersByRole.putIfAbsent(role, 0);
        }
        return usersByRole;
    }

    // Đếm tổng số xe
    public int countVehicles() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Vehicles";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm tổng số vi phạm
    public int countViolations() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Violations";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số lượng vi phạm theo trạng thái
    public Map<String, Integer> countViolationsByStatus() throws SQLException {
        Map<String, Integer> violationsByStatus = new HashMap<>();
        String sql = "SELECT Status, COUNT(*) AS Count FROM Violations GROUP BY Status";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("Status");
                int count = rs.getInt("Count");
                violationsByStatus.put(status, count);
            }
        }
        // Đảm bảo có dữ liệu cho tất cả trạng thái
        String[] statuses = {"Pending", "Resolved"};
        for (String status : statuses) {
            violationsByStatus.putIfAbsent(status, 0);
        }
        return violationsByStatus;
    }

    // Đếm tổng số bản ghi kiểm định
    public int countInspectionRecords() throws SQLException {
        String sql = "SELECT COUNT(*) FROM InspectionRecords";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số lượng kiểm định theo kết quả
    public Map<String, Integer> countInspectionRecordsByResult() throws SQLException {
        Map<String, Integer> inspectionsByResult = new HashMap<>();
        String sql = "SELECT Result, COUNT(*) AS Count FROM InspectionRecords GROUP BY Result";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String result = rs.getString("Result");
                int count = rs.getInt("Count");
                inspectionsByResult.put(result, count);
            }
        }
        // Đảm bảo có dữ liệu cho tất cả kết quả
        String[] results = {"Pass", "Fail"};
        for (String result : results) {
            inspectionsByResult.putIfAbsent(result, 0);
        }
        return inspectionsByResult;
    }

    // Đếm tổng số bản ghi xác minh phương tiện
    public int countVerificationRecords() throws SQLException {
        String sql = "SELECT COUNT(*) FROM VerificationRecords";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số lượng xác minh phương tiện theo trạng thái
    public Map<String, Integer> countVerificationRecordsByStatus() throws SQLException {
        Map<String, Integer> verificationsByStatus = new HashMap<>();
        String sql = "SELECT Status, COUNT(*) AS Count FROM VerificationRecords GROUP BY Status";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("Status");
                int count = rs.getInt("Count");
                verificationsByStatus.put(status, count);
            }
        }
        // Đảm bảo có dữ liệu cho tất cả trạng thái
        String[] statuses = {"Pending", "Approved", "Rejected"};
        for (String status : statuses) {
            verificationsByStatus.putIfAbsent(status, 0);
        }
        return verificationsByStatus;
    }

    // Thống kê số lượng vi phạm theo tháng
    public Map<String, Integer> getViolationsByMonth(int year) throws SQLException {
        Map<String, Integer> violationsByMonth = new HashMap<>();
        String sql = "SELECT MONTH(ViolationDate) AS Month, COUNT(*) AS Count " +
                    "FROM Violations " +
                    "WHERE YEAR(ViolationDate) = ? " +
                    "GROUP BY MONTH(ViolationDate) " +
                    "ORDER BY MONTH(ViolationDate)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("Month");
                    int count = rs.getInt("Count");
                    violationsByMonth.put(String.valueOf(month), count);
                }
            }
        }

        // Đảm bảo có dữ liệu cho tất cả 12 tháng
        for (int i = 1; i <= 12; i++) {
            violationsByMonth.putIfAbsent(String.valueOf(i), 0);
        }
        return violationsByMonth;
    }

    // Thống kê số lượng kiểm định theo tháng
    public Map<String, Integer> getInspectionRecordsByMonth(int year) throws SQLException {
        Map<String, Integer> inspectionsByMonth = new HashMap<>();
        String sql = "SELECT MONTH(InspectionDate) AS Month, COUNT(*) AS Count " +
                    "FROM InspectionRecords " +
                    "WHERE YEAR(InspectionDate) = ? " +
                    "GROUP BY MONTH(InspectionDate) " +
                    "ORDER BY MONTH(InspectionDate)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("Month");
                    int count = rs.getInt("Count");
                    inspectionsByMonth.put(String.valueOf(month), count);
                }
            }
        }

        // Đảm bảo có dữ liệu cho tất cả 12 tháng
        for (int i = 1; i <= 12; i++) {
            inspectionsByMonth.putIfAbsent(String.valueOf(i), 0);
        }
        return inspectionsByMonth;
    }

    // Thống kê số lượng xác minh phương tiện theo tháng
    public Map<String, Integer> getVerificationRecordsByMonth(int year) throws SQLException {
        Map<String, Integer> verificationsByMonth = new HashMap<>();
        String sql = "SELECT MONTH(VerifiedAt) AS Month, COUNT(*) AS Count " +
                    "FROM VerificationRecords " +
                    "WHERE YEAR(VerifiedAt) = ? " +
                    "GROUP BY MONTH(VerifiedAt) " +
                    "ORDER BY MONTH(VerifiedAt)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("Month");
                    int count = rs.getInt("Count");
                    verificationsByMonth.put(String.valueOf(month), count);
                }
            }
        }

        // Đảm bảo có dữ liệu cho tất cả 12 tháng
        for (int i = 1; i <= 12; i++) {
            verificationsByMonth.putIfAbsent(String.valueOf(i), 0);
        }
        return verificationsByMonth;
    }

    // Đếm tổng số trạm kiểm định
    public int countInspectionStations() throws SQLException {
        String sql = "SELECT COUNT(*) FROM InspectionStations";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm tổng số lịch kiểm định
    public int countInspectionSchedules() throws SQLException {
        String sql = "SELECT COUNT(*) FROM InspectionSchedules";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Đếm số lượng lịch kiểm định theo trạng thái
    public Map<String, Integer> countInspectionSchedulesByStatus() throws SQLException {
        Map<String, Integer> schedulesByStatus = new HashMap<>();
        String sql = "SELECT Status, COUNT(*) AS Count FROM InspectionSchedules GROUP BY Status";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("Status");
                int count = rs.getInt("Count");
                schedulesByStatus.put(status, count);
            }
        }
        // Đảm bảo có dữ liệu cho tất cả trạng thái
        String[] statuses = {"Pending", "Confirmed", "Cancelled", "Completed"};
        for (String status : statuses) {
            schedulesByStatus.putIfAbsent(status, 0);
        }
        return schedulesByStatus;
    }
}