package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DBConnection;

public class NotificationDAO {
    private final Connection conn;

    public NotificationDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addNotification(int userId, String message, String type) throws SQLException {
        String sql = "INSERT INTO Notifications (UserID, Message, NotificationType, SentDate, IsRead) VALUES (?, ?, ?, GETDATE(), 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setString(3, type);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}