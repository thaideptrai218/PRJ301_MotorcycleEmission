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
        String sql = "SELECT ir.*, v.PlateNumber, st.Name AS StationName "
                + "FROM InspectionRecords ir "
                + "JOIN Vehicles v ON ir.VehicleID = v.VehicleID "
                + "JOIN InspectionStations st ON ir.StationID = st.StationID "
                + "JOIN Vehicles v2 ON ir.VehicleID = v2.VehicleID "
                + // Đảm bảo liên kết với OwnerID
                "WHERE v2.OwnerID = ? "
                + "ORDER BY ir.InspectionDate DESC";
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
        String sql = "SELECT ir.*, v.PlateNumber, st.Name AS StationName "
                + "FROM InspectionRecords ir "
                + "JOIN Vehicles v ON ir.VehicleID = v.VehicleID "
                + "JOIN InspectionStations st ON ir.StationID = st.StationID "
                + "JOIN InspectionSchedules s ON ir.VehicleID = s.VehicleID AND ir.StationID = s.StationID "
                + "WHERE s.ScheduleID = ?";
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

    public boolean addInspectionRecord(InspectionRecord record) throws SQLException {
        // Kiểm tra đầu vào
        if (record == null) {
            throw new IllegalArgumentException("InspectionRecord cannot be null");
        }
        if (record.getVehicleID() <= 0 || record.getStationID() <= 0 || record.getInspectorID() <= 0) {
            throw new IllegalArgumentException("VehicleID, StationID, and InspectorID must be positive integers");
        }

        String sql = "INSERT INTO InspectionRecords (VehicleID, StationID, InspectorID, InspectionDate, Result, "
                + "CO2Emission, HCEmission, Comments, ExpirationDate, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getVehicleID());
            pstmt.setInt(2, record.getStationID());
            pstmt.setInt(3, record.getInspectorID());

            // Xử lý InspectionDate: Chuyển Date sang Timestamp, kiểm tra null
            java.util.Date inspectionDate = record.getInspectionDate();
            pstmt.setTimestamp(4, inspectionDate != null ? new java.sql.Timestamp(inspectionDate.getTime()) : null);

            pstmt.setString(5, record.getResult());

            // Sử dụng BigDecimal cho CO2Emission và HCEmission
            BigDecimal co2Emission = record.getCo2Emission();
            pstmt.setBigDecimal(6, co2Emission);

            BigDecimal hcEmission = record.getHcEmission();
            pstmt.setBigDecimal(7, hcEmission);

            pstmt.setString(8, record.getComments());

            // Xử lý ExpirationDate: Chuyển Date sang java.sql.Date
            java.util.Date expirationDate = record.getExpirationDate();
            pstmt.setDate(9, expirationDate != null ? new java.sql.Date(expirationDate.getTime()) : null);

            pstmt.setString(10, record.getStatus());

            // Thực thi và kiểm tra số hàng bị ảnh hưởng
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            throw new SQLException("Error adding inspection record: " + e.getMessage(), e);
        }
    }
    
    public List<InspectionRecord> getInspectionRecordsByVehicleId(int vehicleId) throws SQLException {
        List<InspectionRecord> records = new ArrayList<>();
        String sql = "SELECT ir.*, v.PlateNumber, st.Name AS StationName " +
                "FROM InspectionRecords ir " +
                "JOIN Vehicles v ON ir.VehicleID = v.VehicleID " +
                "JOIN InspectionStations st ON ir.StationID = st.StationID " +
                "WHERE ir.VehicleID = ? " +
                "ORDER BY ir.InspectionDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
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
