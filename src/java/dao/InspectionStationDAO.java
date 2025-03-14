package dao;

import model.InspectionStation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class InspectionStationDAO {

    private Connection conn;

    public InspectionStationDAO() {
        conn = new DBConnection().getConnection(); // Giả định DBConnection cung cấp kết nối
    }

    public List<InspectionStation> getAllStations() {
        List<InspectionStation> stations = new ArrayList<>();
        String sql = "SELECT * FROM InspectionStations";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
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
            e.printStackTrace();
            System.out.println("Error fetching InspectionStations: " + e.getMessage());
        }
        return stations;
    }
}
