package dao;

import model.InspectionRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
                    record.setInspectionDate(rs.getTimestamp("InspectionDate").toLocalDateTime());
                    record.setResult(rs.getString("Result"));
                    record.setCO2Emission(rs.getDouble("CO2Emission"));
                    record.setHCEmission(rs.getDouble("HCEmission"));
                    record.setComments(rs.getString("Comments"));
                    record.setExpirationDate(rs.getDate("ExpirationDate").toLocalDate());
                    record.setStatus(rs.getString("Status"));
                    records.add(record);
                }
            }
        }
        return records;
    }
}