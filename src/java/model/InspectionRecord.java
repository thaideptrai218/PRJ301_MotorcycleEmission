package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionRecord {

    private int recordID;
    private int vehicleID;
    private int stationID;
    private int inspectorID;
    private LocalDateTime inspectionDate;
    private String result;
    private double co2Emission;
    private double hcEmission;
    private String comments;
    private LocalDate expirationDate;
    private String status;

    // Getters và Setters
    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public int getInspectorID() {
        return inspectorID;
    }

    public void setInspectorID(int inspectorID) {
        this.inspectorID = inspectorID;
    }

    public LocalDateTime getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDateTime inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getCO2Emission() {
        return co2Emission;
    }

    public void setCO2Emission(double co2Emission) {
        this.co2Emission = co2Emission;
    }

    public double getHCEmission() {
        return hcEmission;
    }

    public void setHCEmission(double hcEmission) {
        this.hcEmission = hcEmission;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
