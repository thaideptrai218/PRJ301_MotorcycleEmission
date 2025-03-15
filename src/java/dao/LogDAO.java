package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DBConnection;

public class LogDAO {
    private final Connection conn;

    public LogDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addLog(int userId, String action) throws SQLException {
        String sql = "INSERT INTO Logs (UserID, Action, Timestamp) VALUES (?, ?, GETDATE())";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}