package model;

import java.util.Date;

public class VerificationRecord {
    private int verificationID;     // Khóa chính, khớp với VerificationID trong bảng
    private int vehicleID;          // Khóa ngoại tham chiếu đến Vehicles
    private Integer verifiedBy;     // Khóa ngoại tham chiếu đến Users, có thể NULL
    private String status;          // "Pending", "Approved", "Rejected"
    private String comments;        // Ghi chú, có thể NULL
    private Date verifiedAt;        // Thời gian xác minh
    private Integer requestID;      // Khóa ngoại tham chiếu đến Requests, có thể NULL (đã đề xuất thay đổi)

    // Getters và Setters
    public int getVerificationID() { return verificationID; }
    public void setVerificationID(int verificationID) { this.verificationID = verificationID; }
    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }
    public Integer getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Integer verifiedBy) { this.verifiedBy = verifiedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public Date getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Date verifiedAt) { this.verifiedAt = verifiedAt; }
    public Integer getRequestID() { return requestID; }
    public void setRequestID(Integer requestID) { this.requestID = requestID; }
}