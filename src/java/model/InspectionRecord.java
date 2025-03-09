package model;

import java.time.LocalDateTime;

public class InspectionRecord {

    private int recordID;
    private int vehicleID;
    private int stationID;
    private int inspectorID;
    private LocalDateTime inspectionDate;
    private String result; // "Pass" or "Fail"
    private double co2Emission; // DECIMAL ánh xạ thành double
    private double hcEmission;
    private String comments;

    // Constructor
    public InspectionRecord() {
    }

    public InspectionRecord(int recordID, int vehicleID, int stationID, int inspectorID, LocalDateTime inspectionDate,
            String result, double co2Emission, double hcEmission, String comments) {
        this.recordID = recordID;
        this.vehicleID = vehicleID;
        this.stationID = stationID;
        this.inspectorID = inspectorID;
        this.inspectionDate = inspectionDate;
        this.result = result;
        this.co2Emission = co2Emission;
        this.hcEmission = hcEmission;
        this.comments = comments;
    }

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

    public double getCo2Emission() {
        return co2Emission;
    }

    public void setCo2Emission(double co2Emission) {
        this.co2Emission = co2Emission;
    }

    public double getHcEmission() {
        return hcEmission;
    }

    public void setHcEmission(double hcEmission) {
        this.hcEmission = hcEmission;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
