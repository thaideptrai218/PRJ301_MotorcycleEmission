package dao;

import model.InspectionStation;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspectionStationDAO {

    private Connection conn;

    public InspectionStationDAO() {
        this.conn = new DBConnection().getConnection(); // Giả định DBConnection cung cấp kết nối
    }

    // Kiểm tra email đã tồn tại
    public boolean isEmailExist(String email) throws SQLException {
        String sql = "SELECT StationID FROM InspectionStations WHERE Email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking email existence: " + e.getMessage(), e);
        }
    }

    // Kiểm tra số điện thoại đã tồn tại
    public boolean isPhoneExist(String phone) throws SQLException {
        String sql = "SELECT StationID FROM InspectionStations WHERE Phone = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking phone existence: " + e.getMessage(), e);
        }
    }

    // Lấy danh sách trạm kiểm định với phân trang, tìm kiếm, và sắp xếp
    public List<InspectionStation> getStations(String searchKeyword, String sortBy, String sortOrder, int page, int pageSize) throws SQLException {
        List<InspectionStation> stations = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT s.StationID, s.Name, s.Address, s.Phone, s.Email, COUNT(ir.RecordID) AS InspectionCount " +
            "FROM InspectionStations s " +
            "LEFT JOIN InspectionRecords ir ON s.StationID = ir.StationID " +
            "WHERE 1=1"
        );

        // Thêm điều kiện tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (s.Name LIKE ? OR s.Address LIKE ? OR s.Phone LIKE ? OR s.Email LIKE ?)");
        }

        sql.append(" GROUP BY s.StationID, s.Name, s.Address, s.Phone, s.Email");

        // Thêm sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder != null && sortOrder.equals("asc") ? "ASC" : "DESC");
        } else {
            sql.append(" ORDER BY s.StationID DESC");
        }

        // Thêm phân trang
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Đặt tham số cho tìm kiếm
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
            }

            // Đặt tham số cho phân trang
            pstmt.setInt(paramIndex++, (page - 1) * pageSize);
            pstmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InspectionStation station = new InspectionStation(
                        rs.getInt("StationID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("InspectionCount")
                    );
                    stations.add(station);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching InspectionStations: " + e.getMessage(), e);
        }
        return stations;
    }

    // Đếm tổng số trạm kiểm định
    public int countStations(String searchKeyword) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM InspectionStations WHERE 1=1");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (Name LIKE ? OR Address LIKE ? OR Phone LIKE ? OR Email LIKE ?)");
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
                pstmt.setString(paramIndex++, keyword);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error counting InspectionStations: " + e.getMessage(), e);
        }
        return 0;
    }

    // Lấy thông tin trạm kiểm định theo ID (đã có sẵn, giữ nguyên)
    public InspectionStation getStationById(int stationId) throws SQLException {
        String sql = "SELECT * FROM InspectionStations WHERE StationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    InspectionStation station = new InspectionStation();
                    station.setStationID(rs.getInt("StationID"));
                    station.setName(rs.getString("Name"));
                    station.setAddress(rs.getString("Address"));
                    station.setPhone(rs.getString("Phone"));
                    station.setEmail(rs.getString("Email"));
                    return station;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching InspectionStation by ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Thêm trạm kiểm định mới
    public boolean addStation(InspectionStation station) throws SQLException {
        String sql = "INSERT INTO InspectionStations (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getAddress());
            pstmt.setString(3, station.getPhone());
            pstmt.setString(4, station.getEmail());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error adding InspectionStation: " + e.getMessage(), e);
        }
    }

    // Cập nhật thông tin trạm kiểm định
    public boolean updateStation(InspectionStation station) throws SQLException {
        String sql = "UPDATE InspectionStations SET Name = ?, Address = ?, Phone = ?, Email = ? WHERE StationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getAddress());
            pstmt.setString(3, station.getPhone());
            pstmt.setString(4, station.getEmail());
            pstmt.setInt(5, station.getStationID());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating InspectionStation: " + e.getMessage(), e);
        }
    }

    // Xóa trạm kiểm định
    public boolean deleteStation(int stationId) throws SQLException {
        String sql = "DELETE FROM InspectionStations WHERE StationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error deleting InspectionStation: " + e.getMessage(), e);
        }
    }

    // Thống kê hoạt động trạm: Số lượng kiểm định và tỷ lệ đạt/không đạt
    public Map<String, Object> getStationStatistics(int stationId) throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT " +
                    "COUNT(*) AS TotalInspections, " +
                    "SUM(CASE WHEN Result = 'Pass' THEN 1 ELSE 0 END) AS PassCount, " +
                    "SUM(CASE WHEN Result = 'Fail' THEN 1 ELSE 0 END) AS FailCount " +
                    "FROM InspectionRecords " +
                    "WHERE StationID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int totalInspections = rs.getInt("TotalInspections");
                    int passCount = rs.getInt("PassCount");
                    int failCount = rs.getInt("FailCount");
                    double passRate = totalInspections > 0 ? (passCount * 100.0 / totalInspections) : 0;
                    double failRate = totalInspections > 0 ? (failCount * 100.0 / totalInspections) : 0;

                    stats.put("totalInspections", totalInspections);
                    stats.put("passCount", passCount);
                    stats.put("failCount", failCount);
                    stats.put("passRate", passRate);
                    stats.put("failRate", failRate);
                } else {
                    stats.put("totalInspections", 0);
                    stats.put("passCount", 0);
                    stats.put("failCount", 0);
                    stats.put("passRate", 0.0);
                    stats.put("failRate", 0.0);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching station statistics: " + e.getMessage(), e);
        }
        return stats;
    }

    // Phương thức getAllStations cũ (giữ lại nếu cần)
    public List<InspectionStation> getAllStations() throws SQLException {
        List<InspectionStation> stations = new ArrayList<>();
        String sql = "SELECT * FROM InspectionStations";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                InspectionStation station = new InspectionStation();
                station.setStationID(rs.getInt("StationID"));
                station.setName(rs.getString("Name"));
                station.setAddress(rs.getString("Address"));
                station.setPhone(rs.getString("Phone"));
                station.setEmail(rs.getString("Email"));
                stations.add(station);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching InspectionStations: " + e.getMessage(), e);
        }
        return stations;
    }
}