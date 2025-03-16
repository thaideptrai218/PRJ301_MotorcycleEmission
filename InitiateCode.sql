-- Tạo Cơ sở Dữ liệu
CREATE DATABASE MotorcycleEmissionDB;
GO

USE MotorcycleEmissionDB;
GO

-- 1. Bảng Users (Người dùng)
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Role NVARCHAR(20) NOT NULL CHECK (Role IN ('Owner', 'Inspector', 'Station', 'Police', 'Admin')),
    Phone NVARCHAR(15) NOT NULL
);
GO

-- 2. Bảng Vehicles (Phương tiện)
CREATE TABLE Vehicles (
    VehicleID INT IDENTITY(1,1) PRIMARY KEY,
    OwnerID INT NOT NULL,
    PlateNumber NVARCHAR(15) NOT NULL UNIQUE,
    Brand NVARCHAR(50) NOT NULL,
    Model NVARCHAR(50) NOT NULL,
    ManufactureYear INT NOT NULL,
    EngineNumber NVARCHAR(100) NOT NULL,
    FOREIGN KEY (OwnerID) REFERENCES Users(UserID)
);
GO

-- 3. Bảng InspectionStations (Trạm kiểm định)
CREATE TABLE InspectionStations (
    StationID INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Address NVARCHAR(MAX) NOT NULL,
    Phone NVARCHAR(15) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE
);
GO

-- 4. Bảng InspectionSchedules (Lịch kiểm định)
CREATE TABLE InspectionSchedules (
    ScheduleID INT IDENTITY(1,1) PRIMARY KEY,
    VehicleID INT NOT NULL,
    StationID INT NOT NULL,
    OwnerID INT NOT NULL,
    ScheduleDate DATETIME2 NOT NULL,
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'Confirmed', 'Cancelled', 'Completed')) DEFAULT 'Pending',
    CreatedAt DATETIME2 DEFAULT GETDATE(),
    RequestID INT NOT NULL,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (StationID) REFERENCES InspectionStations(StationID),
    FOREIGN KEY (OwnerID) REFERENCES Users(UserID),
    FOREIGN KEY (RequestID) REFERENCES Requests(RequestID)
);
GO

-- 5. Bảng InspectionRecords (Kết quả kiểm định)
CREATE TABLE InspectionRecords (
    RecordID INT IDENTITY(1,1) PRIMARY KEY,
    VehicleID INT NOT NULL,
    StationID INT NOT NULL,
    InspectorID INT NOT NULL,
    InspectionDate DATETIME2 DEFAULT GETDATE(),
    Result NVARCHAR(10) NOT NULL CHECK (Result IN ('Pass', 'Fail')),
    CO2Emission DECIMAL(5,2) NOT NULL,
    HCEmission DECIMAL(5,2) NOT NULL,
    Comments NVARCHAR(MAX),
    ExpirationDate DATE NOT NULL,
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'InProgress', 'Completed')) DEFAULT 'Pending',
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (StationID) REFERENCES InspectionStations(StationID),
    FOREIGN KEY (InspectorID) REFERENCES Users(UserID)
);
GO

-- 6. Bảng Violations (Vi phạm)
CREATE TABLE Violations (
    ViolationID INT IDENTITY(1,1) PRIMARY KEY,
    VehicleID INT NOT NULL,
    PoliceID INT NOT NULL,
    ViolationDate DATETIME2 DEFAULT GETDATE(),
    Reason NVARCHAR(MAX) NOT NULL,
    PenaltyAmount DECIMAL(10,2),
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'Resolved')) DEFAULT 'Pending',
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (PoliceID) REFERENCES Users(UserID)
);
GO

-- 7. Bảng VerificationRecords (Xác minh phương tiện)
CREATE TABLE VerificationRecords (
    VerificationID INT IDENTITY(1,1) PRIMARY KEY,
    VehicleID INT NOT NULL,
    VerifiedBy INT,
    Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'Approved', 'Rejected')) DEFAULT 'Pending',
    Comments NVARCHAR(MAX),
    VerifiedAt DATETIME2 DEFAULT GETDATE(),
    RequestID INT NOT NULL,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (VerifiedBy) REFERENCES Users(UserID),
    FOREIGN KEY (RequestID) REFERENCES Requests(RequestID)
);
GO

-- 8. Bảng Notifications (Thông báo)
CREATE TABLE Notifications (
    NotificationID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Message NVARCHAR(MAX) NOT NULL,
    NotificationType NVARCHAR(20) NOT NULL CHECK (NotificationType IN ('Reminder', 'Violation', 'Result', 'Schedule')),
    SentDate DATETIME2 DEFAULT GETDATE(),
    IsRead BIT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- 9. Bảng Logs (Lịch sử hoạt động)
CREATE TABLE Logs (
    LogID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Action NVARCHAR(100) NOT NULL,
    Timestamp DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- 10. Bảng Requests (Yêu cầu)
CREATE TABLE Requests (
    RequestID INT IDENTITY(1,1) PRIMARY KEY,
    CreatedBy INT NOT NULL,
    AssignedTo INT NULL,
    Type NVARCHAR(50) NOT NULL CHECK (Type IN ('VehicleVerification', 'InspectionSchedule', 'Maintenance', 'Other')),
    CreateDate DATETIME2 DEFAULT GETDATE(),
    Message NVARCHAR(MAX) NOT NULL,
    Status NVARCHAR(20) NOT NULL DEFAULT 'Pending' CHECK (Status IN ('Pending', 'Processing', 'Completed', 'Rejected')),
    UpdatedAt DATETIME2 NULL,
    Priority NVARCHAR(20) NULL CHECK (Priority IN ('Low', 'Medium', 'High')) DEFAULT 'Medium',
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    FOREIGN KEY (AssignedTo) REFERENCES Users(UserID)
);
GO

-- 11. Tạo Index tối ưu truy vấn
CREATE INDEX idx_vehicles_plate ON Vehicles(PlateNumber);
CREATE INDEX idx_inspections_vehicle ON InspectionRecords(VehicleID);
CREATE INDEX idx_schedules_vehicle ON InspectionSchedules(VehicleID);
CREATE INDEX idx_violations_vehicle ON Violations(VehicleID);
GO

-- 12. Chèn dữ liệu mẫu vào bảng Users
INSERT INTO Users (FullName, Email, Password, Role, Phone) VALUES
('Nguyen Van A', 'owner@example.com', 'hashed_password', 'Owner', '0901234567'),
('Le Thi B', 'inspector@example.com', 'hashed_password', 'Inspector', '0901234568'),
('Station 1', 'station1@example.com', 'hashed_password', 'Station', '0901234569'),
('Tran Van C', 'police@example.com', 'hashed_password', 'Police', '0901234570'),
('Admin User', 'admin@example.com', 'hashed_password', 'Admin', '0901234571');
GO

-- 13. Chèn dữ liệu mẫu vào bảng Vehicles
INSERT INTO Vehicles (OwnerID, PlateNumber, Brand, Model, ManufactureYear, EngineNumber) VALUES
(1, '29H1-54311', 'Yamaha', 'Sirius', 2019, 'ENG654333');
GO

-- 14. Chèn dữ liệu vào bảng InspectionStations
INSERT INTO InspectionStations (Name, Address, Phone, Email) VALUES
('Station Hanoi 1', '123 Main Road, Hanoi', '0909876543', 'station1@example.com'),
('Station Hanoi 2', '456 Side Road, Hanoi', '0909876544', 'station2@example.com');
GO

-- 15. Chèn dữ liệu vào bảng InspectionSchedules
INSERT INTO InspectionSchedules (VehicleID, StationID, OwnerID, ScheduleDate, Status, RequestID) VALUES
(1, 1, 1, '2025-03-20 10:00:00', 'Pending', 1),
(1, 1, 1, '2025-03-15 14:00:00', 'Completed', 2);
GO
