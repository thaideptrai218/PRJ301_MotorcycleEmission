package dao;

import model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class NotificationDAO {
    private final Connection conn;

    public NotificationDAO() {
        this.conn = new DBConnection().getConnection();
    }

    public boolean addNotification(int userId, String message, String notificationType) throws SQLException {
        String sql = "INSERT INTO Notifications (UserID, Message, NotificationType, SentDate, IsRead) " +
                     "VALUES (?, ?, ?, GETDATE(), 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setString(3, notificationType);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Notification> getNotificationsByUserId(int userId, int page, int pageSize) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM Notifications WHERE UserID = ? ORDER BY SentDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, offset);
            pstmt.setInt(3, pageSize);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("NotificationID"));
                    notification.setUserID(rs.getInt("UserID"));
                    notification.setMessage(rs.getString("Message"));
                    notification.setNotificationType(rs.getString("NotificationType"));
                    notification.setIsRead(rs.getBoolean("IsRead"));
                    notification.setSentDate(rs.getTimestamp("SentDate"));
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    public int getTotalNotifications(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE UserID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean markAsRead(int notificationId, boolean isRead) throws SQLException {
        String sql = "UPDATE Notifications SET IsRead = ? WHERE NotificationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isRead);
            pstmt.setInt(2, notificationId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteNotification(int notificationId) throws SQLException {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notificationId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public int getUnreadCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE UserID = ? AND IsRead = 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    public List<Notification> getTopUnreadNotifications(int userId, int limit) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Notifications WHERE UserID = ? AND IsRead = 0 ORDER BY SentDate DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("NotificationID"));
                    notification.setUserID(rs.getInt("UserID"));
                    notification.setMessage(rs.getString("Message"));
                    notification.setNotificationType(rs.getString("NotificationType"));
                    notification.setIsRead(rs.getBoolean("IsRead"));
                    notification.setSentDate(rs.getTimestamp("SentDate"));
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }
}