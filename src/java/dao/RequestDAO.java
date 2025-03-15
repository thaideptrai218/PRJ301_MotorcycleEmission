package dao;

import model.Request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class RequestDAO {

    private final Connection conn;

    public RequestDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public void addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO Requests (CreatedBy, AssignedTo, Type, Message, Status, Priority, VehicleID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getCreatedBy());
            pstmt.setObject(2, request.getAssignedTo());
            pstmt.setString(3, request.getType());
            pstmt.setString(4, request.getMessage());
            pstmt.setString(5, request.getStatus());
            pstmt.setString(6, request.getPriority());
            pstmt.setObject(7, request.getVehicleID()); // Có thể null
            pstmt.executeUpdate();
        }
    }

    public List<Request> getRequestsByUserId(int userId) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Requests WHERE CreatedBy = ? OR AssignedTo = ? ORDER BY CreateDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setRequestID(rs.getInt("RequestID"));
                    request.setCreatedBy(rs.getInt("CreatedBy"));
                    request.setAssignedTo(rs.getObject("AssignedTo") != null ? rs.getInt("AssignedTo") : null);
                    request.setType(rs.getString("Type"));
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    request.setPriority(rs.getString("Priority"));
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    public void updateRequestStatus(int requestId, String status, Integer assignedTo) throws SQLException {
        String sql = "UPDATE Requests SET Status = ?, AssignedTo = ?, UpdatedAt = GETDATE() WHERE RequestID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setObject(2, assignedTo); // Có thể null
            pstmt.setInt(3, requestId);
            pstmt.executeUpdate();
        }
    }

    public Request getRequestById(int requestId) throws SQLException {
        String sql = "SELECT * FROM Requests WHERE RequestID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Request request = new Request();
                    request.setRequestID(rs.getInt("RequestID"));
                    request.setCreatedBy(rs.getInt("CreatedBy"));
                    request.setAssignedTo(rs.getObject("AssignedTo") != null ? rs.getInt("AssignedTo") : null);
                    request.setType(rs.getString("Type"));
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    request.setPriority(rs.getString("Priority"));
                    request.setVehicleID(rs.getObject("VehicleID") != null ? rs.getInt("VehicleID") : null);
                    return request;
                }
            }
        }
        return null;
    }

    public List<Request> getPendingRequestsByUserId(int userId) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Requests WHERE (CreatedBy = ? OR AssignedTo = ?) AND Status = 'Pending' ORDER BY CreateDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setRequestID(rs.getInt("RequestID"));
                    request.setCreatedBy(rs.getInt("CreatedBy"));
                    request.setAssignedTo(rs.getObject("AssignedTo") != null ? rs.getInt("AssignedTo") : null);
                    request.setType(rs.getString("Type"));
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    request.setPriority(rs.getString("Priority"));
                    request.setVehicleID(rs.getObject("VehicleID") != null ? rs.getInt("VehicleID") : null);
                    requests.add(request);
                }
            }
        }
        return requests;
    }
}
