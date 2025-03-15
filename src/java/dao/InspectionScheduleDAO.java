package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;

public class InspectionScheduleDAO {

    private final Connection conn;

    public InspectionScheduleDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addSchedule(int vehicleId, int stationId, int ownerId, String scheduleDate) throws SQLException {
        String sql = "INSERT INTO InspectionSchedules (VehicleID, StationID, OwnerID, ScheduleDate, Status, CreatedAt) "
                + "VALUES (?, ?, ?, ?, 'Pending', GETDATE())";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, stationId);
            pstmt.setInt(3, ownerId);
            pstmt.setString(4, scheduleDate);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public String getStationName(int stationId) throws SQLException {
        String sql = "SELECT Name FROM InspectionStations WHERE StationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name");
                }
            }
        }
        return null;
    }

    // Phương thức kiểm tra xem phương tiện đã có lịch hẹn chưa (tùy chọn)
    public boolean hasPendingSchedule(int vehicleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM InspectionSchedules WHERE VehicleID = ? AND Status = 'Pending'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
