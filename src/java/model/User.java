package model;

public class User {
    private int userID;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String phone;
    private boolean isLocked; // Thêm thuộc tính isLocked

    public User() {
    }

    public User(int userID, String fullName, String email, String password, String role, String phone) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.isLocked = false;
    }

    public User(int userID, String fullName, String email, String password, String role, String phone, boolean isLocked) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.isLocked = isLocked;
    }

    // Getters và Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}