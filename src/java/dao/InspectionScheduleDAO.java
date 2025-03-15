package dao;

import model.InspectionSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class InspectionScheduleDAO {
    private final Connection conn;

    public InspectionScheduleDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addInspectionSchedule(InspectionSchedule inspectionSchedule) throws SQLException {
        String sql = "INSERT INTO InspectionSchedules (VehicleID, StationID, OwnerID, ScheduleDate, Status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inspectionSchedule.getVehicleID());
            pstmt.setInt(2, inspectionSchedule.getStationID());
            pstmt.setInt(3, inspectionSchedule.getOwnerID());
            pstmt.setDate(4, new java.sql.Date(inspectionSchedule.getScheduleDate().getTime()));
            pstmt.setString(5, inspectionSchedule.getStatus());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public List<InspectionSchedule> getInspectionSchedulesByOwnerId(int ownerId) throws SQLException {
        List<InspectionSchedule> inspectionSchedules = new ArrayList<>();
        String sql = "SELECT s.ScheduleID, s.VehicleID, s.StationID, s.OwnerID, s.ScheduleDate, s.Status, s.CreatedAt, " +
                     "v.PlateNumber, st.Name AS StationName " +
                     "FROM InspectionSchedules s " +
                     "JOIN Vehicles v ON s.VehicleID = v.VehicleID " +
                     "JOIN InspectionStations st ON s.StationID = st.StationID " +
                     "WHERE s.OwnerID = ? " +
                     "ORDER BY s.CreatedAt DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
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
                    schedule.setPlateNumber(rs.getString("PlateNumber"));
                    schedule.setStationName(rs.getString("StationName"));
                    inspectionSchedules.add(schedule);
                }
            }
        }
        return inspectionSchedules;
    }
}