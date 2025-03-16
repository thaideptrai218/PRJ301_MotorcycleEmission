package dao;

import model.VerificationRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class VerificationRecordDAO {

    private final Connection conn;

    public VerificationRecordDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addVerification(int vehicleId) throws SQLException {
        String sql = "INSERT INTO VerificationRecords (VehicleID, Status, RequestID) VALUES (?, ?, NULL)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            pstmt.setString(2, "Pending");
            return pstmt.executeUpdate() > 0;
        }
    }

    public void addVerificationRecord(VerificationRecord record) throws SQLException {
        String sql = "INSERT INTO VerificationRecords (VehicleID, Status, RequestID, VerifiedBy, Comments) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getVehicleID());
            pstmt.setString(2, record.getStatus());
            pstmt.setObject(3, record.getRequestID()); // Có thể NULL
            pstmt.setObject(4, record.getVerifiedBy()); // Có thể NULL
            pstmt.setString(5, record.getComments()); // Có thể NULL
            pstmt.executeUpdate();
        }
    }

    public VerificationRecord getVerificationRecordByRequestId(int requestId) throws SQLException {
        String sql = "SELECT * FROM VerificationRecords WHERE RequestID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    VerificationRecord record = new VerificationRecord();
                    record.setVerificationID(rs.getInt("VerificationID"));
                    record.setVehicleID(rs.getInt("VehicleID"));
                    record.setVerifiedBy(rs.getObject("VerifiedBy") != null ? rs.getInt("VerifiedBy") : null);
                    record.setStatus(rs.getString("Status"));
                    record.setComments(rs.getString("Comments"));
                    record.setVerifiedAt(rs.getTimestamp("VerifiedAt"));
                    record.setRequestID(rs.getObject("RequestID") != null ? rs.getInt("RequestID") : null);
                    return record;
                }
            }
        }
        return null;
    }

    public void updateStatus(int verificationId, String status) throws SQLException {
        String sql = "UPDATE VerificationRecords SET Status = ?, VerifiedAt = GETDATE() WHERE VerificationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, verificationId);
            pstmt.executeUpdate();
        }
    }

    public List<VerificationRecord> getVerificationRecordsByUserId(int userId) throws SQLException {
        List<VerificationRecord> records = new ArrayList<>();
        String sql = "SELECT vr.* FROM VerificationRecords vr "
                + "JOIN Vehicles v ON vr.VehicleID = v.VehicleID "
                + "WHERE v.OwnerID = ? OR vr.VerifiedBy = ? "
                + "ORDER BY vr.VerifiedAt DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId); // OwnerID
            pstmt.setInt(2, userId); // VerifiedBy (Inspector/Station)
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    VerificationRecord record = new VerificationRecord();
                    record.setVerificationID(rs.getInt("VerificationID"));
                    record.setVehicleID(rs.getInt("VehicleID"));
                    record.setVerifiedBy(rs.getObject("VerifiedBy") != null ? rs.getInt("VerifiedBy") : null);
                    record.setStatus(rs.getString("Status"));
                    record.setComments(rs.getString("Comments"));
                    record.setVerifiedAt(rs.getTimestamp("VerifiedAt"));
                    record.setRequestID(rs.getObject("RequestID") != null ? rs.getInt("RequestID") : null);
                    records.add(record);
                }
            }
        }
        return records;
    }
}
