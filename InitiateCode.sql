CREATE DATABASE MotorcycleEmissionDB3;
GO

USE MotorcycleEmissionDB3;
GO

-- 1.1. Bảng Users (Người dùng)
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Role NVARCHAR(20) NOT NULL CHECK (Role IN ('Owner', 'Inspector', 'Station', 'Police', 'Admin')),
    Phone NVARCHAR(15) NOT NULL,
	isLocked BIT DEFAULT 0
);
GO

Insert INTO Users (FullName, Email, Password, Role, Phone) VALUES ('AdminVIP', 'admin@gmail.com', 'admin', 'Admin', 6969696969)
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
	VehicleID INT NULL,
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserID),
    FOREIGN KEY (AssignedTo) REFERENCES Users(UserID)
);

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



CREATE TABLE Logs (
    LogID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Action NVARCHAR(100) NOT NULL,
    Timestamp DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

INSERT INTO InspectionStations (Name, Address, Phone, Email) VALUES
('Station Hanoi 4', '789 Main Road, Hanoi', '1234567890', 'station3@example.com'),
('Station Hanoi 5', '901 Side Road, Hanoi', '0123456789', 'station4@example.com');
GO
SELECT * FROM InspectionStations
