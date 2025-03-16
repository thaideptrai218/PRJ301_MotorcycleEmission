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

    public int addRequest(Request request) throws SQLException {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new SQLException("Message không được để trống.");
        }
        if (!List.of("VehicleVerification", "InspectionSchedule", "Maintenance", "Other").contains(request.getType())) {
            throw new SQLException("Type không hợp lệ: " + request.getType());
        }
        String sql = "INSERT INTO Requests (CreatedBy, AssignedTo, Type, Message, Status, Priority, VehicleID) OUTPUT INSERTED.RequestID VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getCreatedBy());
            pstmt.setObject(2, request.getAssignedTo());
            pstmt.setString(3, request.getType());
            pstmt.setString(4, request.getMessage());
            pstmt.setString(5, request.getStatus());
            pstmt.setString(6, request.getPriority());
            pstmt.setObject(7, request.getVehicleID());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("RequestID");
                }
            }
        }
        throw new SQLException("Không thể lấy RequestID sau khi thêm.");
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
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setPriority(rs.getString("Priority"));
                    request.setVehicleID(rs.getObject("VehicleID") != null ? rs.getInt("VehicleID") : null);
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    requests.add(request);
                }
            }
        }
        return requests;
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
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setPriority(rs.getString("Priority"));
                    request.setVehicleID(rs.getObject("VehicleID") != null ? rs.getInt("VehicleID") : null);
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    return request;
                }
            }
        }
        return null;
    }

    public void updateRequestStatus(int requestId, String status, Integer assignedTo) throws SQLException {
        String sql = "UPDATE Requests SET Status = ?, AssignedTo = ?, UpdatedAt = GETDATE() WHERE RequestID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setObject(2, assignedTo);
            pstmt.setInt(3, requestId);
            pstmt.executeUpdate();
        }
    }

    // Lấy tất cả yêu cầu VehicleVerification với bộ lọc và phân trang
    public List<Request> getVehicleVerificationRequests(String status, String searchKeyword, String fromDate, String toDate, String sortBy, String sortOrder, int page, int pageSize) throws SQLException {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Requests WHERE Type = 'VehicleVerification'");
        List<Object> params = new ArrayList<>();

        // Thêm điều kiện Status nếu có
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }

        // Thêm điều kiện tìm kiếm từ khóa
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (Message LIKE ? OR CAST(VehicleID AS NVARCHAR) LIKE ?)");
            params.add("%" + searchKeyword + "%");
            params.add("%" + searchKeyword + "%");
        }

        // Thêm điều kiện khoảng thời gian
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append(" AND CreateDate >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append(" AND CreateDate <= ?");
            params.add(toDate + " 23:59:59");
        }

        // Thêm sắp xếp
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            sql.append(" ORDER BY ");
            if ("createDate".equals(sortBy)) {
                sql.append("CreateDate");
            } else if ("vehicleId".equals(sortBy)) {
                sql.append("VehicleID");
            }
            sql.append(" ").append("asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC");
        } else {
            sql.append(" ORDER BY CreateDate DESC");
        }

        // Thêm phân trang
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Request request = new Request();
                    request.setRequestID(rs.getInt("RequestID"));
                    request.setCreatedBy(rs.getInt("CreatedBy"));
                    request.setAssignedTo(rs.getObject("AssignedTo") != null ? rs.getInt("AssignedTo") : null);
                    request.setType(rs.getString("Type"));
                    request.setMessage(rs.getString("Message"));
                    request.setStatus(rs.getString("Status"));
                    request.setPriority(rs.getString("Priority"));
                    request.setVehicleID(rs.getObject("VehicleID") != null ? rs.getInt("VehicleID") : null);
                    request.setCreateDate(rs.getTimestamp("CreateDate"));
                    request.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    // Đếm tổng số yêu cầu VehicleVerification để hỗ trợ phân trang
    public int countVehicleVerificationRequests(String status, String searchKeyword, String fromDate, String toDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Requests WHERE Type = 'VehicleVerification'");
        List<Object> params = new ArrayList<>();

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (Message LIKE ? OR CAST(VehicleID AS NVARCHAR) LIKE ?)");
            params.add("%" + searchKeyword + "%");
            params.add("%" + searchKeyword + "%");
        }
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append(" AND CreateDate >= ?");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append(" AND CreateDate <= ?");
            params.add(toDate + " 23:59:59");
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}