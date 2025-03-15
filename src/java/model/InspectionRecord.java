package model;

import java.math.BigDecimal;
import java.util.Date;

public class InspectionRecord {

    private int recordID;
    private int vehicleID;
    private int stationID;
    private int inspectorID;
    private Date inspectionDate;
    private String result;
    private java.math.BigDecimal co2Emission;
    private java.math.BigDecimal hcEmission;
    private String comments;
    private Date expirationDate;
    private String status;
    private String plateNumber; // Thêm từ Vehicles
    private String stationName;// Getters và Setters

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

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BigDecimal getCo2Emission() {
        return co2Emission;
    }

    public void setCo2Emission(BigDecimal co2Emission) {
        this.co2Emission = co2Emission;
    }

    public BigDecimal getHcEmission() {
        return hcEmission;
    }

    public void setHcEmission(BigDecimal hcEmission) {
        this.hcEmission = hcEmission;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
