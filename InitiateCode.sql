CREATE DATABASE MotorcycleEmissionDB;
GO

USE MotorcycleEmissionDB;
GO

-- 1.1. Bảng Users (Người dùng)
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Role NVARCHAR(20) NOT NULL CHECK (Role IN ('Owner', 'Inspector', 'Station', 'Police', 'Admin')),
    Phone NVARCHAR(15) NOT NULL,
);
GO

SELECT * FROM Users

-- 1.2. Bảng Vehicles (Phương tiện)
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

-- 1.3. Bảng InspectionStations (Cơ sở kiểm định)
CREATE TABLE InspectionStations (
    StationID INT IDENTITY(1,1) PRIMARY KEY,
    Name	 NVARCHAR(100) NOT NULL,
    Address NVARCHAR(MAX) NOT NULL,
    Phone NVARCHAR(15) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE
);
GO

-- 1.4. Bảng InspectionSchedules (Lịch kiểm định)
	CREATE TABLE InspectionSchedules (
		ScheduleID INT IDENTITY(1,1) PRIMARY KEY,
		VehicleID INT NOT NULL,
		StationID INT NOT NULL,
		OwnerID INT NOT NULL,
		ScheduleDate DATETIME2 NOT NULL,
		Status NVARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'Confirmed', 'Cancelled', 'Completed')) DEFAULT 'Pending',
		CreatedAt DATETIME2 DEFAULT GETDATE(),
		FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
		FOREIGN KEY (StationID) REFERENCES InspectionStations(StationID),
		FOREIGN KEY (OwnerID) REFERENCES Users(UserID),
		RequestID INT NOT NULL,
		FOREIGN KEY (RequestID) REFERENCES Requests(RequestID)
	);
GO

SELECT * FROM InspectionSchedules

-- 1.5. Bảng InspectionRecords (Kết quả kiểm định)
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

SELECT * FROM InspectionRecords

-- 1.6. Bảng Violations (Vi phạm)
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

-- 1.7. Bảng VerificationRecords (Xác minh phương tiện)
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
SELECT * FROM VerificationRecords


-- 1.8. Bảng Notifications (Thông báo)
CREATE TABLE Notifications (
    NotificationID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Message NVARCHAR(MAX) NOT NULL,
    NotificationType NVARCHAR(20) NOT NULL CHECK (NotificationType IN ('Reminder', 'Violation', 'Result', 'Schedule', 'Verify')),
    SentDate DATETIME2 DEFAULT GETDATE(),
    IsRead BIT DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO



-- 1.9. Bảng Logs (Lịch sử hoạt động)
CREATE TABLE Logs (
    LogID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Action NVARCHAR(100) NOT NULL,
    Timestamp DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Thêm index để tối ưu truy vấn
CREATE INDEX idx_vehicles_plate ON Vehicles(PlateNumber);
CREATE INDEX idx_inspections_vehicle ON InspectionRecords(VehicleID);
CREATE INDEX idx_schedules_vehicle ON InspectionSchedules(VehicleID);
CREATE INDEX idx_violations_vehicle ON Violations(VehicleID);
GO

-- Thêm dữ liệu vào bảng Users
INSERT INTO Users (FullName, Email, Password, Role, Phone) VALUES
('Nguyen Van A', 'owner@example.com', 'hashed_password', 'Owner', '0901234567'),
('Le Thi B', 'inspector@example.com', 'hashed_password', 'Inspector', '0901234568'),
('Station 1', 'station1@example.com', 'hashed_password', 'Station', '0901234569'),
('Tran Van C', 'police@example.com', 'hashed_password', 'Police', '0901234570'),
('Admin User', 'admin@example.com', 'hashed_password', 'Admin', '0901234571');
GO
SELECT * FROM Users

-- Thêm dữ liệu vào bảng Vehicles
INSERT INTO Vehicles (OwnerID, PlateNumber, Brand, Model, ManufactureYear, EngineNumber) VALUES
(6, '29H1-54311', 'Yamaha', 'Sirius', 2019, 'ENG654333');
GO
SELECT * FROM Vehicles

-- Thêm dữ liệu vào bảng InspectionStations
INSERT INTO InspectionStations (Name, Address, Phone, Email) VALUES
('Station Hanoi 4', '789 Main Road, Hanoi', '1234567890', 'station3@example.com'),
('Station Hanoi 5', '901 Side Road, Hanoi', '0123456789', 'station4@example.com');
GO
SELECT * FROM InspectionStations

-- Thêm dữ liệu vào bảng VerificationRecords
INSERT INTO VerificationRecords (VehicleID, Status, Comments) VALUES
(7, 'Approved', 'Đã xác minh');

GO
SELECT * FROM VerificationRecords

-- Thêm dữ liệu vào bảng InspectionSchedules
INSERT INTO InspectionSchedules (VehicleID, StationID, OwnerID, ScheduleDate, Status) VALUES
(1, 1, 1, '2025-03-20 10:00:00', 'Pending'),
(1, 1, 1, '2025-03-15 14:00:00', 'Completed');
GO
SELECT * FROM InspectionSchedules



-- Thêm dữ liệu vào bảng InspectionRecords
INSERT INTO InspectionRecords (VehicleID, StationID, InspectorID, InspectionDate, Result, CO2Emission, HCEmission, Comments, ExpirationDate, Status) VALUES
(1, 1, 2, '2025-03-15 14:00:00', 'Pass', 45.5, 30.2, 'Đạt tiêu chuẩn', '2025-09-15', 'Completed');
GO
SELECT * FROM InspectionRecords

-- Thêm dữ liệu vào bảng Violations
INSERT INTO Violations (VehicleID, PoliceID, ViolationDate, Reason, PenaltyAmount, Status) VALUES
(1, 4, '2025-03-16 09:00:00', 'Hết hạn kiểm định', 500000, 'Pending');
GO

-- Thêm dữ liệu vào bảng Notifications
INSERT INTO Notifications (UserID, Message, NotificationType, SentDate, IsRead) VALUES
(1, 'Phương tiện của bạn đã được thêm và đang chờ xác minh', 'Result', '2025-03-14 08:00:00', 0),
(1, 'Bạn có lịch hẹn kiểm định vào 2025-03-20 10:00', 'Schedule', '2025-03-14 09:00:00', 0),
(1, 'Phương tiện của bạn có vi phạm: Hết hạn kiểm định', 'Violation', '2025-03-16 09:00:00', 0),
(6, 'Phương tiện (29A-12345) của bạn đã được thêm và đang chờ xác minh.', 'Result', '2025-03-15 10:00:00', 0),
(6, 'Lịch kiểm định vào 2025-03-20 tại Station A đã được lên lịch.', 'Schedule', '2025-03-14 15:30:00', 0),
(6, 'Kết quả kiểm định: Đạt.', 'Result', '2025-03-13 09:15:00', 1),
(6, 'Nhắc nhở: Vui lòng kiểm tra phương tiện trước ngày 2025-03-25.', 'Reminder', '2025-03-12 14:00:00', 0),
(6, 'Phát hiện vi phạm tốc độ vào 2025-03-11.', 'Violation', '2025-03-11 11:45:00', 1),
(6, 'Lịch kiểm định bổ sung vào 2025-03-18 tại Station B.', 'Schedule', '2025-03-10 08:30:00', 0),
(6, 'Kết quả kiểm định lần trước: Không đạt, cần kiểm tra lại.', 'Result', '2025-03-09 13:20:00', 1);
GO
SELECT * FROM Notifications

-- Thêm dữ liệu vào bảng Logs
INSERT INTO Logs (UserID, Action, Timestamp) VALUES
(1, 'Add Vehicle', '2025-03-14 08:00:00'),
(1, 'Schedule Inspection', '2025-03-14 09:00:00');
GO
SELECT * FROM Logs

CREATE TABLE Requests (
    RequestID INT IDENTITY(1,1) PRIMARY KEY,
    CreatedBy INT NOT NULL, -- ID của người gửi (Owner, Station, Inspector)
    AssignedTo INT NULL, -- ID của Station hoặc Inspector được phân công (liên kết với Users)
    Type NVARCHAR(50) NOT NULL CHECK (Type IN ('VehicleVerification', 'InspectionSchedule', 'Maintenance', 'Other')),
    CreateDate DATETIME2 DEFAULT GETDATE(),
    Message NVARCHAR(MAX) NOT NULL,
    Status NVARCHAR(20) NOT NULL DEFAULT 'Pending' CHECK (Status IN ('Pending', 'Processing', 'Completed', 'Rejected')),
    UpdatedAt DATETIME2 NULL,
    Priority NVARCHAR(20) NULL CHECK (Priority IN ('Low', 'Medium', 'High')) DEFAULT 'Medium',
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    FOREIGN KEY (AssignedTo) REFERENCES Users(UserID)
);
SELECT * FROM Requests

