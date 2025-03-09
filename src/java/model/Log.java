package model;

import java.time.LocalDateTime;

public class Log {
    private int logID;
    private int userID;
    private String action;
    private LocalDateTime timestamp;

    // Constructor
    public Log() {}

    public Log(int logID, int userID, String action, LocalDateTime timestamp) {
        this.logID = logID;
        this.userID = userID;
        this.action = action;
        this.timestamp = timestamp;
    }

    // Getters và Setters
    public int getLogID() { return logID; }
    public void setLogID(int logID) { this.logID = logID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}