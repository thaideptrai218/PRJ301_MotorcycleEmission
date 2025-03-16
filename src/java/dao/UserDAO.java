package dao;

import model.User;
import utils.DBConnection;

import java.sql.*;

public class UserDAO {

    public boolean isEmailExist(String email) {
        String sql = "SELECT userID FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneExist(String phone) {
        String sql = "SELECT userID FROM users WHERE phone = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (fullName, email, password, role, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getPhone());

            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password); // You should hash and compare instead of storing plain text passwords

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("UserID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Phone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
        }

        return user;
    }
    
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("UserID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Phone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
        }

        return user;
    }
    
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Phone")
                    );
                }
            }
        }
        return null;
    }
}
