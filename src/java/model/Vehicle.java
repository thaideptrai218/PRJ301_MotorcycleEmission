package model;

public class Vehicle {
    private int vehicleID;
    private int ownerID;
    private String plateNumber;
    private String brand;
    private String model;
    private int manufactureYear; // Giữ int, tương ứng với YEAR trong SQL Server
    private String engineNumber;

    // Constructor không tham số
    public Vehicle() {
    }

    // Constructor đầy đủ tham số
    public Vehicle(int vehicleID, int ownerID, String plateNumber, String brand, String model, int manufactureYear, String engineNumber) {
        this.vehicleID = vehicleID;
        this.ownerID = ownerID;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.engineNumber = engineNumber;
    }

    // Getters và Setters
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        if (manufactureYear >= 1900 && manufactureYear <= 9999) {
            this.manufactureYear = manufactureYear;
        } else {
            throw new IllegalArgumentException("Manufacture year must be between 1900 and 9999");
        }
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }
}