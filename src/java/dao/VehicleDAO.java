package dao;

import model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class VehicleDAO {

    private final Connection conn;

    public VehicleDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public int addVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO Vehicles (OwnerID, PlateNumber, Brand, Model, ManufactureYear, EngineNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vehicle.getOwnerID());
            pstmt.setString(2, vehicle.getPlateNumber());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getManufactureYear());
            pstmt.setString(6, vehicle.getEngineNumber());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về vehicleId
                    }
                }
            }
            return -1; // Trả về -1 nếu thất bại
        }
    }

    public List<Vehicle> getVehiclesByOwnerId(int ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM Vehicles WHERE OwnerID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID(rs.getInt("VehicleID"));
                    vehicle.setOwnerID(rs.getInt("OwnerID"));
                    vehicle.setPlateNumber(rs.getString("PlateNumber"));
                    vehicle.setBrand(rs.getString("Brand"));
                    vehicle.setModel(rs.getString("Model"));
                    vehicle.setManufactureYear(rs.getInt("ManufactureYear"));
                    vehicle.setEngineNumber(rs.getString("EngineNumber"));
                    vehicles.add(vehicle);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByOwnerIdWithVerificationStatus(int ownerId) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT v.*, vr.Status AS VerificationStatus "
                + "FROM Vehicles v "
                + "LEFT JOIN VerificationRecords vr ON v.VehicleID = vr.VehicleID "
                + "WHERE v.OwnerID = ? "
                + "ORDER BY v.VehicleID DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID(rs.getInt("VehicleID"));
                    vehicle.setOwnerID(rs.getInt("OwnerID"));
                    vehicle.setPlateNumber(rs.getString("PlateNumber"));
                    vehicle.setBrand(rs.getString("Brand"));
                    vehicle.setModel(rs.getString("Model"));
                    vehicle.setManufactureYear(rs.getInt("ManufactureYear"));
                    vehicle.setEngineNumber(rs.getString("EngineNumber"));
                    vehicle.setVerificationStatus(rs.getString("VerificationStatus"));
                    vehicles.add(vehicle);
                }
            }
        }
        return vehicles;
    }
}
