package model;

import java.util.Date;

public class Request {
    private int requestID;
    private int createdBy;
    private Integer assignedTo;
    private String type;
    private Date createDate;
    private String message;
    private String status;
    private Date updatedAt;
    private String priority;
    private Integer vehicleID; // Thêm thuộc tính mới

    // Getters và Setters
    public int getRequestID() { return requestID; }
    public void setRequestID(int requestID) { this.requestID = requestID; }
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Integer getVehicleID() { return vehicleID; }
    public void setVehicleID(Integer vehicleID) { this.vehicleID = vehicleID; }
}