package dao;

import model.InspectionStation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class InspectionStationDAO {

    private Connection conn;

    public InspectionStationDAO() {
        this.conn = new DBConnection().getConnection(); // Giả định DBConnection cung cấp kết nối
    }

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
}