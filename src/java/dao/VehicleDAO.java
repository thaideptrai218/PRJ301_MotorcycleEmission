package dao;

import model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class VehicleDAO {
    private Connection conn;

    public VehicleDAO() {
        conn = new DBConnection().getConnection(); // Giả định DBConnection cung cấp kết nối
    }

    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicles (ownerID, plateNumber, brand, model, manufactureYear, engineNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vehicle.getOwnerID());
            pstmt.setString(2, vehicle.getPlateNumber());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getManufactureYear());
            pstmt.setString(6, vehicle.getEngineNumber());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> getVehiclesByOwnerId(int ownerID) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM Vehicles WHERE ownerID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleID(rs.getInt("vehicleID"));
                vehicle.setOwnerID(rs.getInt("ownerID"));
                vehicle.setPlateNumber(rs.getString("plateNumber"));
                vehicle.setBrand(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setManufactureYear(rs.getInt("manufactureYear"));
                vehicle.setEngineNumber(rs.getString("engineNumber"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
}