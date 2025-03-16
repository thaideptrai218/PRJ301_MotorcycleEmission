package model;

import java.util.Date;

public class InspectionSchedule {
    private String plateNumber;
    private String stationName;
    private int scheduleID;
    private int vehicleID;
    private int stationID;
    private int ownerID;
    private Date scheduleDate;
    private String status; // "Pending", "Confirmed", "Completed", "Cancelled"
    private Date createdAt;
    private Integer requestID; // Thêm thuộc tính mới

    // Getters và Setters
    public int getScheduleID() { return scheduleID; }
    public void setScheduleID(int scheduleID) { this.scheduleID = scheduleID; }
    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }
    public int getStationID() { return stationID; }
    public void setStationID(int stationID) { this.stationID = stationID; }
    public int getOwnerID() { return ownerID; }
    public void setOwnerID(int ownerID) { this.ownerID = ownerID; }
    public Date getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(Date scheduleDate) { this.scheduleDate = scheduleDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Integer getRequestID() { return requestID; }
    public void setRequestID(Integer requestID) { this.requestID = requestID; }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    
    
}