package dao;

import model.InspectionSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utils.DBConnection;

public class InspectionScheduleDAO {

    private final Connection conn;

    public InspectionScheduleDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addInspectionSchedule(InspectionSchedule schedule) throws SQLException {
        String sql = "INSERT INTO InspectionSchedules (VehicleID, StationID, OwnerID, ScheduleDate, Status, RequestID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getVehicleID());
            pstmt.setInt(2, schedule.getStationID());
            pstmt.setInt(3, schedule.getOwnerID());
            pstmt.setTimestamp(4, new java.sql.Timestamp(schedule.getScheduleDate().getTime()));
            pstmt.setString(5, schedule.getStatus());
            pstmt.setInt(6, schedule.getRequestID()); // RequestID là NOT NULL
            return pstmt.executeUpdate() > 0;
        }
    }

    public InspectionSchedule getScheduleByRequestId(int requestId) throws SQLException {
        String sql = "SELECT s.*, v.PlateNumber, st.Name AS StationName "
                + "FROM InspectionSchedules s "
                + "JOIN Vehicles v ON s.VehicleID = v.VehicleID "
                + "JOIN InspectionStations st ON s.StationID = st.StationID "
                + "WHERE s.RequestID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    InspectionSchedule schedule = new InspectionSchedule();
                    schedule.setScheduleID(rs.getInt("ScheduleID"));
                    schedule.setVehicleID(rs.getInt("VehicleID"));
                    schedule.setStationID(rs.getInt("StationID"));
                    schedule.setOwnerID(rs.getInt("OwnerID"));
                    schedule.setScheduleDate(rs.getTimestamp("ScheduleDate"));
                    schedule.setStatus(rs.getString("Status"));
                    schedule.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    schedule.setRequestID(rs.getInt("RequestID"));
                    schedule.setPlateNumber(rs.getString("PlateNumber"));
                    schedule.setStationName(rs.getString("StationName"));
                    return schedule;
                }
            }
        }
        return null;
    }

    public ArrayList<InspectionSchedule> getScheduleByUserID(int userID) throws SQLException {
        ArrayList<InspectionSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, v.PlateNumber, st.Name AS StationName "
                + "FROM InspectionSchedules s "
                + "JOIN Vehicles v ON s.VehicleID = v.VehicleID "
                + "JOIN InspectionStations st ON s.StationID = st.StationID "
                + "WHERE s.OwnerID = ? OR s.StationID = ? "
                + "ORDER BY s.ScheduleDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InspectionSchedule schedule = new InspectionSchedule();
                    schedule.setScheduleID(rs.getInt("ScheduleID"));
                    schedule.setVehicleID(rs.getInt("VehicleID"));
                    schedule.setStationID(rs.getInt("StationID"));
                    schedule.setOwnerID(rs.getInt("OwnerID"));
                    schedule.setScheduleDate(rs.getTimestamp("ScheduleDate"));
                    schedule.setStatus(rs.getString("Status"));
                    schedule.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    schedule.setRequestID(rs.getInt("RequestID"));
                    schedule.setPlateNumber(rs.getString("PlateNumber"));
                    schedule.setStationName(rs.getString("StationName"));
                    schedules.add(schedule);
                }
            }
        }
        return schedules;
    }

    public InspectionSchedule getLatestPendingScheduleByVehicleId(int vehicleId) throws SQLException {
        String sql = "SELECT TOP 1 s.*, v.PlateNumber, st.Name AS StationName "
                + "FROM InspectionSchedules s "
                + "JOIN Vehicles v ON s.VehicleID = v.VehicleID "
                + "JOIN InspectionStations st ON s.StationID = st.StationID "
                + "WHERE s.VehicleID = ? AND s.Status = 'Pending' "
                + "ORDER BY s.CreatedAt DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    InspectionSchedule schedule = new InspectionSchedule();
                    schedule.setScheduleID(rs.getInt("ScheduleID"));
                    schedule.setVehicleID(rs.getInt("VehicleID"));
                    schedule.setStationID(rs.getInt("StationID"));
                    schedule.setOwnerID(rs.getInt("OwnerID"));
                    schedule.setScheduleDate(rs.getTimestamp("ScheduleDate"));
                    schedule.setStatus(rs.getString("Status"));
                    schedule.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    schedule.setRequestID(rs.getInt("RequestID"));
                    schedule.setPlateNumber(rs.getString("PlateNumber"));
                    schedule.setStationName(rs.getString("StationName"));
                    return schedule;
                }
            }
        }
        return null;
    }

    public void updateStatus(int scheduleId, String status) throws SQLException {
        String sql = "UPDATE InspectionSchedules SET Status = ? WHERE ScheduleID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, scheduleId);
            pstmt.executeUpdate();
        }
    }
}
