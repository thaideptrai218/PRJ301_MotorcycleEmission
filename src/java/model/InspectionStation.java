package model;

public class InspectionStation {
    private int stationID;
    private String name;
    private String address;
    private String phone;
    private String email;

    // Constructor
    public InspectionStation() {}

    public InspectionStation(int stationID, String name, String address, String phone, String email) {
        this.stationID = stationID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Getters và Setters
    public int getStationID() { return stationID; }
    public void setStationID(int stationID) { this.stationID = stationID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}