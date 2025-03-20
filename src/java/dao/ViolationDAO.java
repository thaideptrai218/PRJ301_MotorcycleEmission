package dao;

import model.Violation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class ViolationDAO {

    private final Connection conn;

    public ViolationDAO() {
        this.conn = new DBConnection().getConnection();
    }

    // Thêm một vi phạm mới
    public boolean addViolation(Violation violation) throws SQLException {
        String sql = "INSERT INTO Violations (VehicleID, PoliceID, ViolationDate, Reason, PenaltyAmount, Status) " +
                    "VALUES (?, ?, GETDATE(), ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, violation.getVehicleID());
            pstmt.setInt(2, violation.getPoliceID());
            pstmt.setString(3, violation.getReason());
            pstmt.setBigDecimal(4, violation.getPenaltyAmount());
            pstmt.setString(5, violation.getStatus());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Lấy danh sách vi phạm theo VehicleID
    public List<Violation> getViolationsByVehicleId(int vehicleId) throws SQLException {
        List<Violation> violations = new ArrayList<>();
        String sql = "SELECT v.*, u.FullName AS PoliceName " +
                    "FROM Violations v " +
                    "JOIN Users u ON v.PoliceID = u.UserID " +
                    "WHERE v.VehicleID = ? " +
                    "ORDER BY v.ViolationDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Violation violation = new Violation();
                    violation.setViolationID(rs.getInt("ViolationID"));
                    violation.setVehicleID(rs.getInt("VehicleID"));
                    violation.setPoliceID(rs.getInt("PoliceID"));
                    violation.setViolationDate(rs.getTimestamp("ViolationDate"));
                    violation.setReason(rs.getString("Reason"));
                    violation.setPenaltyAmount(rs.getBigDecimal("PenaltyAmount"));
                    violation.setStatus(rs.getString("Status"));
                    violation.setPoliceName(rs.getString("PoliceName"));
                    violations.add(violation);
                }
            }
        }
        return violations;
    }
    
    public List<Violation> getViolationsByOwnerId(int ownerId, String statusFilter, String sortOrder) throws SQLException {
        List<Violation> violations = new ArrayList<>();
        String sql = "SELECT v.*, u.FullName AS PoliceName, veh.PlateNumber " +
                    "FROM Violations v " +
                    "JOIN Users u ON v.PoliceID = u.UserID " +
                    "JOIN Vehicles veh ON v.VehicleID = veh.VehicleID " +
                    "WHERE veh.OwnerID = ? " +
                    (statusFilter != null && !statusFilter.isEmpty() ? "AND v.Status = ? " : "") +
                    "ORDER BY v.ViolationDate " + (sortOrder != null && sortOrder.equals("asc") ? "ASC" : "DESC");

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            if (statusFilter != null && !statusFilter.isEmpty()) {
                pstmt.setString(2, statusFilter);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Violation violation = new Violation();
                    violation.setViolationID(rs.getInt("ViolationID"));
                    violation.setVehicleID(rs.getInt("VehicleID"));
                    violation.setPoliceID(rs.getInt("PoliceID"));
                    violation.setViolationDate(rs.getTimestamp("ViolationDate"));
                    violation.setReason(rs.getString("Reason"));
                    violation.setPenaltyAmount(rs.getBigDecimal("PenaltyAmount"));
                    violation.setStatus(rs.getString("Status"));
                    violation.setPoliceName(rs.getString("PoliceName"));
                    violation.setPlateNumber(rs.getString("PlateNumber")); // Thêm PlateNumber vào model Violation
                    violations.add(violation);
                }
            }
        }
        return violations;
    }
}