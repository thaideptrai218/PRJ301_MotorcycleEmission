CREATE DATABASE MotorcycleEmissionDB;
USE MotorcycleEmissionDB;

CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),           -- ID tự tăng, INT đủ cho hàng triệu bản ghi
    FullName NVARCHAR(100) NOT NULL,                -- NVARCHAR thay VARCHAR để hỗ trợ Unicode (tiếng Việt)
    Email VARCHAR(100) NOT NULL UNIQUE,             -- VARCHAR đủ cho email, giữ nguyên
    Password VARCHAR(255) NOT NULL,                 -- VARCHAR(255) để lưu mật khẩu đã mã hóa (SHA-256, BCrypt)
    Role VARCHAR(20) NOT NULL CHECK (Role IN ('Owner', 'Inspector', 'Station', 'Police')), -- VARCHAR(20) đủ cho enum
    Phone VARCHAR(15) NOT NULL,                     -- VARCHAR(15) đủ cho số điện thoại
);

ALTER TABLE Users
DROP COLUMN Address;

CREATE TABLE Vehicles (
    VehicleID INT PRIMARY KEY IDENTITY(1,1),
    OwnerID INT NOT NULL,
    PlateNumber VARCHAR(15) NOT NULL UNIQUE,        -- VARCHAR(15) đủ cho biển số xe
    Brand NVARCHAR(50) NOT NULL,                    -- NVARCHAR cho tên hãng (có thể có ký tự đặc biệt)
    Model NVARCHAR(50) NOT NULL,                    -- NVARCHAR cho mẫu xe
    ManufactureYear SMALLINT NOT NULL,              -- SMALLINT thay YEAR (2 bytes, từ 0-65535, đủ cho năm sản xuất)
    EngineNumber VARCHAR(100) NOT NULL,             -- VARCHAR(100) đủ cho số động cơ
    FOREIGN KEY (OwnerID) REFERENCES Users(UserID)
);

CREATE TABLE InspectionStations (
    StationID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,                    -- NVARCHAR cho tên cơ sở
    Address NVARCHAR(255) NOT NULL,                 -- NVARCHAR thay TEXT, giới hạn 255 ký tự
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE InspectionRecords (
    RecordID INT PRIMARY KEY IDENTITY(1,1),
    VehicleID INT NOT NULL,
    StationID INT NOT NULL,
    InspectorID INT NOT NULL,
    InspectionDate DATETIME DEFAULT GETDATE(),
    Result VARCHAR(10) NOT NULL CHECK (Result IN ('Pass', 'Fail')), -- VARCHAR(10) đủ cho Pass/Fail
    CO2Emission DECIMAL(5,2) NOT NULL,              -- DECIMAL(5,2): 3 số nguyên, 2 thập phân (VD: 123.45 g/km)
    HCEmission DECIMAL(5,2) NOT NULL,               -- Tương tự
    Comments NVARCHAR(500),                         -- NVARCHAR thay TEXT, giới hạn 500 ký tự
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (StationID) REFERENCES InspectionStations(StationID),
    FOREIGN KEY (InspectorID) REFERENCES Users(UserID)
);

CREATE TABLE Notifications (
    NotificationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    Message NVARCHAR(500) NOT NULL,                 -- NVARCHAR cho nội dung thông báo
    SentDate DATETIME DEFAULT GETDATE(),
    IsRead BIT DEFAULT 0,                           -- BIT cho true/false (0/1)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Logs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    Action NVARCHAR(100) NOT NULL,                  -- NVARCHAR để hỗ trợ tiếng Việt
    Timestamp DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

SELECT * FROM users

INSERT INTO users (fullName, email, password, role, phone) VALUES ('Thai Dinh', 'ThaiDinh@gmail.com', '123123', 'Owner', '1231231231')