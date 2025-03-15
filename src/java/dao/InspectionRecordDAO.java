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
        String sql = "SELECT ir.* FROM InspectionRecords ir " +
                     "JOIN Vehicles v ON ir.VehicleID = v.VehicleID " +
                     "WHERE v.OwnerID = ? " +
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
                    record.setInspectionDate(rs.getTimestamp("InspectionDate")); // Giữ nguyên kiểu Date
                    record.setResult(rs.getString("Result"));
                    record.setCO2Emission(rs.getBigDecimal("CO2Emission")); // Sử dụng BigDecimal
                    record.setHCEmission(rs.getBigDecimal("HCEmission")); // Sử dụng BigDecimal
                    record.setComments(rs.getString("Comments"));
                    record.setExpirationDate(rs.getDate("ExpirationDate")); // Giữ nguyên kiểu Date
                    record.setStatus(rs.getString("Status"));
                    records.add(record);
                }
            }
        }
        return records;
    }

    // Phương thức khác cũng cần được cập nhật để phù hợp với model
    public List<InspectionRecord> getInspectionRecordsByScheduleId(int scheduleId) throws SQLException {
        List<InspectionRecord> records = new ArrayList<>();
        String sql = "SELECT ir.* " +
                     "FROM InspectionRecords ir " +
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
                    record.setInspectionDate(rs.getTimestamp("InspectionDate")); // Giữ nguyên kiểu Date
                    record.setResult(rs.getString("Result"));
                    record.setCO2Emission(rs.getBigDecimal("CO2Emission")); // Sử dụng BigDecimal
                    record.setHCEmission(rs.getBigDecimal("HCEmission")); // Sử dụng BigDecimal
                    record.setComments(rs.getString("Comments"));
                    record.setExpirationDate(rs.getDate("ExpirationDate")); // Giữ nguyên kiểu Date
                    record.setStatus(rs.getString("Status"));
                    records.add(record);
                }
            }
        }
        return records;
    }
}