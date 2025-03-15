package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;

public class VerificationDAO {

    private final Connection conn;

    public VerificationDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addVerification(int vehicleId) throws SQLException {
        String sql = "INSERT INTO VerificationRecords (VehicleID, Status, Comments) VALUES (?, 'Pending', ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            pstmt.setString(2, "Chờ xác minh");
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean isVehicleApproved(int vehicleId) throws SQLException {
        String sql = "SELECT Status FROM VerificationRecords WHERE VehicleID = ? AND Status = 'Approved'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Trả về true nếu có bản ghi với trạng thái 'Approved'
            }
        }
    }
}
