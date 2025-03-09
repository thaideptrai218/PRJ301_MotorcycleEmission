package model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationID;
    private int userID;
    private String message;
    private LocalDateTime sentDate;
    private boolean isRead;

    // Constructor
    public Notification() {}

    public Notification(int notificationID, int userID, String message, LocalDateTime sentDate, boolean isRead) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.message = message;
        this.sentDate = sentDate;
        this.isRead = isRead;
    }

    // Getters và Setters
    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
}