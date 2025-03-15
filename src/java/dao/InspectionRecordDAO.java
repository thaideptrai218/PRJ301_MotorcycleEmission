package dao;

import model.InspectionRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import utils.DBConnection;

public class InspectionRecordDAO {
    private final Connection conn;

    public InspectionRecordDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public List<InspectionRecord> getInspectionRecordsByOwnerId(int ownerId) throws SQLException {
        List<InspectionRecord> records = new ArrayList<>();
        String sql = "SELECT ir.*, v.PlateNumber, st.Name AS StationName " +
                     "FROM InspectionRecords ir " +
                     "JOIN Vehicles v ON ir.VehicleID = v.VehicleID " +
                     "JOIN InspectionStations st ON ir.StationID = st.StationID " +
                     "JOIN Vehicles v2 ON ir.VehicleID = v2.VehicleID " + // Đảm bảo liên kết với OwnerID
                     "WHERE v2.OwnerID = ? " +
                     "ORDER BY ir.InspectionDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InspectionRecord record = new InspectionRecord();
                    record.setRecordID(rs.getInt("RecordID"));
                    record.setVehicleID(rs.getInt("VehicleID"));
                    record.setStationID(rs.getInt("StationID"));
                    record.setInspectorID(rs.getInt("InspectorID"));
                    record.setInspectionDate(rs.getTimestamp("InspectionDate"));
                    record.setResult(rs.getString("Result"));
                    record.setCo2Emission(rs.getBigDecimal("CO2Emission"));
                    record.setHcEmission(rs.getBigDecimal("HCEmission"));
                    record.setComments(rs.getString("Comments"));
                    record.setExpirationDate(rs.getDate("ExpirationDate"));
                    record.setStatus(rs.getString("Status"));
                    record.setPlateNumber(rs.getString("PlateNumber"));
                    record.setStationName(rs.getString("StationName"));
                    records.add(record);
                }
            }
        }
        return records;
    }

    // Giữ nguyên phương thức khác nếu cần
    public List<InspectionRecord> getInspectionRecordsByScheduleId(int scheduleId) throws SQLException {
        List<InspectionRecord> records = new ArrayList<>();
        String sql = "SELECT ir.*, v.PlateNumber, st.Name AS StationName " +
                     "FROM InspectionRecords ir " +
                     "JOIN Vehicles v ON ir.VehicleID = v.VehicleID " +
                     "JOIN InspectionStations st ON ir.StationID = st.StationID " +
                     "JOIN InspectionSchedules s ON ir.VehicleID = s.VehicleID AND ir.StationID = s.StationID " +
                     "WHERE s.ScheduleID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InspectionRecord record = new InspectionRecord();
                    record.setRecordID(rs.getInt("RecordID"));
                    record.setVehicleID(rs.getInt("VehicleID"));
                    record.setStationID(rs.getInt("StationID"));
                    record.setInspectorID(rs.getInt("InspectorID"));
                    record.setInspectionDate(rs.getTimestamp("InspectionDate"));
                    record.setResult(rs.getString("Result"));
                    record.setCo2Emission(rs.getBigDecimal("CO2Emission"));
                    record.setHcEmission(rs.getBigDecimal("HCEmission"));
                    record.setComments(rs.getString("Comments"));
                    record.setExpirationDate(rs.getDate("ExpirationDate"));
                    record.setStatus(rs.getString("Status"));
                    record.setPlateNumber(rs.getString("PlateNumber"));
                    record.setStationName(rs.getString("StationName"));
                    records.add(record);
                }
            }
        }
        return records;
    }
}