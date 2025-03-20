package dao;

import model.User;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    
    public List<User> getAllUsers(String roleFilter, String lockedFilter) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users " +
                    (roleFilter != null && !roleFilter.isEmpty() || lockedFilter != null && !lockedFilter.isEmpty() ? "WHERE " : "") +
                    (roleFilter != null && !roleFilter.isEmpty() ? "Role = ? " : "") +
                    (roleFilter != null && !roleFilter.isEmpty() && lockedFilter != null && !lockedFilter.isEmpty() ? "AND " : "") +
                    (lockedFilter != null && !lockedFilter.isEmpty() ? "isLocked = ? " : "");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (roleFilter != null && !roleFilter.isEmpty()) {
                stmt.setString(paramIndex++, roleFilter);
            }
            if (lockedFilter != null && !lockedFilter.isEmpty()) {
                stmt.setBoolean(paramIndex, lockedFilter.equals("locked"));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("UserID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Phone"),
                            rs.getBoolean("isLocked")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }

    // Phương thức mới: Cập nhật thông tin người dùng
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, Phone = ?, Role = ?, isLocked = ? WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getRole());
            stmt.setBoolean(5, user.isLocked());
            stmt.setInt(6, user.getUserID());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // Phương thức mới: Xóa người dùng
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    // Phương thức mới: Khóa/Mở khóa tài khoản
    public boolean toggleLockUser(int userId, boolean lock) throws SQLException {
        String sql = "UPDATE Users SET isLocked = ? WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, lock);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
    
    public List<User> getUsers(String searchKeyword, String roleFilter, String lockedFilter, String sortBy, String sortOrder, int page, int pageSize) throws SQLException {
        List<User> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Users WHERE 1=1");

        // Thêm điều kiện tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ? OR Phone LIKE ?)");
        }

        // Thêm điều kiện lọc theo vai trò
        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" AND Role = ?");
        }

        // Thêm điều kiện lọc theo trạng thái khóa
        if (lockedFilter != null && !lockedFilter.isEmpty()) {
            sql.append(" AND isLocked = ?");
        }

        // Thêm sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder != null && sortOrder.equals("asc") ? "ASC" : "DESC");
        } else {
            sql.append(" ORDER BY UserID DESC");
        }

        // Thêm phân trang
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Đặt tham số cho tìm kiếm
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
            }

            // Đặt tham số cho bộ lọc vai trò
            if (roleFilter != null && !roleFilter.isEmpty()) {
                stmt.setString(paramIndex++, roleFilter);
            }

            // Đặt tham số cho bộ lọc trạng thái khóa
            if (lockedFilter != null && !lockedFilter.isEmpty()) {
                stmt.setBoolean(paramIndex++, lockedFilter.equals("locked"));
            }

            // Đặt tham số cho phân trang
            stmt.setInt(paramIndex++, (page - 1) * pageSize);
            stmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("UserID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Phone"),
                            rs.getBoolean("isLocked")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }

    // Phương thức mới: Đếm tổng số người dùng (để tính số trang)
    public int countUsers(String searchKeyword, String roleFilter, String lockedFilter) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Users WHERE 1=1");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ? OR Phone LIKE ?)");
        }

        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" AND Role = ?");
        }

        if (lockedFilter != null && !lockedFilter.isEmpty()) {
            sql.append(" AND isLocked = ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
            }

            if (roleFilter != null && !roleFilter.isEmpty()) {
                stmt.setString(paramIndex++, roleFilter);
            }

            if (lockedFilter != null && !lockedFilter.isEmpty()) {
                stmt.setBoolean(paramIndex, lockedFilter.equals("locked"));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
