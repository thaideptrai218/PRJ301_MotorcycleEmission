package model;

import java.util.Date;

public class Notification {
    private int notificationID;
    private int userID;
    private String message;
    private String notificationType;
    private boolean isRead;
    private Date sentDate;

    // Getters và Setters
    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public boolean getIsRead() { return isRead; }  // Đổi từ isRead() thành getIsRead()
    public void setIsRead(boolean isRead) { this.isRead = isRead; }  // Đổi từ setRead() thành setIsRead()
    public Date getSentDate() { return sentDate; }
    public void setSentDate(Date sentDate) { this.sentDate = sentDate; }
}