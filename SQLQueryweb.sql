USE master;
GO

IF DB_ID('Sport4') IS NOT NULL
BEGIN
    ALTER DATABASE Sport4 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE Sport4;
END
GO

CREATE DATABASE Sport4;
GO
USE Sport4;
GO

CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Role NVARCHAR(20) NOT NULL
        CHECK (Role IN ('ADMIN','EMPLOYEE','CUSTOMER'))
);
GO


INSERT INTO Users (Username, Password, FullName, Email, Role) VALUES
('admin1', '123', N'Quản trị viên', 'admin@store.com', 'ADMIN'),
('employee1', '123', N'Nhân viên bán hàng', 'employee@store.com', 'EMPLOYEE'),
('customer1', '123', N'Nguyễn Văn A', 'customer@store.com', 'CUSTOMER');
GO


CREATE TABLE Categories (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(50) NOT NULL
);
GO

INSERT INTO Categories (CategoryName) VALUES
(N'Giầy'),
(N'Quần áo'),
(N'Balo'),
(N'Áo đá bóng');
GO

UPDATE Categories
SET CategoryName = N'Giầy'
WHERE CategoryName = N'Giày';

CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(100) NOT NULL,
    CategoryID INT NOT NULL,
    Description NVARCHAR(MAX),
    Price MONEY NOT NULL,
    Image NVARCHAR(255),
    Status NVARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);
GO

INSERT INTO Products (ProductName, CategoryID, Description, Price, Image) VALUES
-- GIÀY (CategoryID = 1)
(N'Giày Nike Pegasus', 1, N'Giày chạy bộ Nike', 120.00, 'shoe1.jpg'),
(N'Giày Adidas Ultraboost', 1, N'Giày chạy bộ Adidas', 150.00, 'shoe2.jpg'),
(N'Giày Nike Air Force 1', 1, N'Giày sneaker Nike', 130.00, 'shoe3.jpg'),
(N'Giày Adidas Stan Smith', 1, N'Giày sneaker Adidas', 120.00, 'shoe4.jpg'),
(N'Giày Puma Velocity Nitro', 1, N'Giày chạy bộ Puma', 110.00, 'shoe5.jpg'),

-- ÁO (CategoryID = 2)
(N'Áo thun Nike Dri-FIT', 2, N'Áo thun thể thao Nike', 35.00, 'shirt1.jpg'),
(N'Áo hoodie Adidas', 2, N'Áo hoodie Adidas', 60.00, 'shirt2.jpg'),
(N'Áo khoác gió Nike', 2, N'Áo khoác gió thể thao', 70.00, 'shirt3.jpg'),

-- ÁO ĐÁ BÓNG (CategoryID = 4)
(N'Áo đá bóng Barcelona 2024', 4, N'Áo đấu sân nhà Barcelona', 90.00, 'football1.jpg'),
(N'Áo đá bóng Real Madrid 2024', 4, N'Áo đấu sân nhà Real Madrid', 90.00, 'football2.jpg'),
(N'Áo đá bóng Manchester United', 4, N'Áo đấu sân nhà MU', 85.00, 'football3.jpg'),
(N'Áo đá bóng Arsenal', 4, N'Áo đấu sân nhà Arsenal', 85.00, 'football4.jpg'),
(N'Áo đá bóng PSG', 4, N'Áo đấu sân nhà PSG', 88.00, 'football5.jpg'),
(N'Áo đá bóng Bayern Munich', 4, N'Áo đấu sân nhà Bayern Munich', 87.00, 'football6.jpg'),
(N'Áo đá bóng Liverpool', 4, N'Áo đấu sân nhà Liverpool', 89.00, 'football7.jpg'),
(N'Áo đá bóng Juventus', 4, N'Áo đấu sân nhà Juventus', 86.00, 'football8.jpg'),
(N'Áo đá bóng Việt Nam sân nhà', 4, N'Áo đội tuyển Việt Nam sân nhà', 75.00, 'football9.jpg'),
(N'Áo đá bóng Việt Nam sân khách', 4, N'Áo đội tuyển Việt Nam sân khách', 75.00, 'football10.jpg'),

-- BALO (CategoryID = 3)
(N'Balo Nike Air', 3, N'Balo thể thao Nike', 60.00, 'bag1.jpg'),
(N'Balo Adidas Power', 3, N'Balo thể thao Adidas', 50.00, 'bag2.jpg'),
(N'Balo Puma Phase', 3, N'Balo Puma Phase', 45.00, 'bag3.jpg'),
(N'Balo Under Armour Hustle', 3, N'Balo Under Armour Hustle', 60.00, 'bag4.jpg');
GO

CREATE TABLE ProductVariants (
    VariantID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT NOT NULL,
    Size NVARCHAR(20) NOT NULL,
    Color NVARCHAR(20) NOT NULL,
    Price MONEY NOT NULL,
    Stock INT NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
GO


INSERT INTO ProductVariants (ProductID, Size, Color, Price, Stock) VALUES
-- GIÀY
(1, '40', 'Black', 120.00, 10),
(1, '41', 'White', 120.00, 8),
(2, '41', 'Black', 150.00, 6),
(2, '42', 'Gray', 150.00, 5),

-- ÁO & ÁO ĐÁ BÓNG
(6, 'M', 'Black', 35.00, 20),
(6, 'L', 'White', 35.00, 15),
(9, 'M', 'Blue', 90.00, 12),
(10, 'L', 'White', 90.00, 10),
(11, 'L', 'Red', 85.00, 14),

-- BALO
(15, 'Free', 'Black', 60.00, 25),
(16, 'Free', 'Blue', 50.00, 20);
GO



CREATE TABLE Carts (
    CartID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    Status NVARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE | CHECKED_OUT
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO


CREATE TABLE CartItems (
    CartItemID INT IDENTITY(1,1) PRIMARY KEY,
    CartID INT NOT NULL,
    VariantID INT NOT NULL,
    Quantity INT NOT NULL,
    FOREIGN KEY (CartID) REFERENCES Carts(CartID),
    FOREIGN KEY (VariantID) REFERENCES ProductVariants(VariantID)
);
GO

CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL, -- CUSTOMER
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalPrice MONEY NOT NULL,
    PaymentMethod NVARCHAR(20) 
        CHECK (PaymentMethod IN ('COD','BANK')),
    PaymentStatus NVARCHAR(20) 
        CHECK (PaymentStatus IN ('UNPAID','PAID')),
    Status NVARCHAR(20) DEFAULT 'NEW',
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO


CREATE TABLE OrderDetails (
    OrderID INT NOT NULL,
    VariantID INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice MONEY NOT NULL,
    PRIMARY KEY (OrderID, VariantID),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (VariantID) REFERENCES ProductVariants(VariantID)
);
GO


CREATE TABLE Reviews (
    ReviewID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT NOT NULL,
    UserID INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(255),
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Cart của customer1
INSERT INTO Carts (UserID) VALUES (3);

INSERT INTO CartItems (CartID, VariantID, Quantity)
VALUES (1,1,1);

-- Đơn hàng demo
INSERT INTO Orders (UserID, TotalPrice, PaymentMethod, PaymentStatus)
VALUES (3,3200000,'COD','UNPAID');

INSERT INTO OrderDetails (OrderID, VariantID, Quantity, UnitPrice)
VALUES (1,1,1,3200000);
GO
INSERT INTO ProductVariants (ProductID, Size, Color, Price, Stock) VALUES
(1,'40','Black',120,20),
(1,'40','White',120,15),
(1,'41','Black',120,18),
(1,'41','White',120,14),
(1,'42','Black',120,16),
(1,'42','White',120,12),
(1,'43','Black',120,10),
(1,'43','White',120,8),
(1,'44','Black',120,6),
(1,'44','White',120,5),


(2,'40','Black',150,12),
(2,'40','Gray',150,10),
(2,'41','Black',150,14),
(2,'41','Gray',150,12),
(2,'42','Black',150,10),
(2,'42','Gray',150,9),
(2,'43','Black',150,8),
(2,'43','Gray',150,7),
(2,'44','Black',150,6),
(2,'44','Gray',150,5),

(3,'40','Blue',110,15),
(3,'40','Black',110,14),
(3,'41','Blue',110,13),
(3,'41','Black',110,12),
(3,'42','Blue',110,11),
(3,'42','Black',110,10),
(3,'43','Blue',110,9),
(3,'43','Black',110,8),
(3,'44','Blue',110,7),
(3,'44','Black',110,6),

(4,'S','Black',35,30),
(4,'S','White',35,25),
(4,'M','Black',35,28),
(4,'M','White',35,24),
(4,'L','Black',35,22),
(4,'L','White',35,20),
(4,'XL','Black',35,18),
(4,'XL','White',35,15),
(4,'XXL','Black',35,10),
(4,'XXL','White',35,8),

(5,'S','Gray',60,15),
(5,'S','Black',60,14),
(5,'M','Gray',60,13),
(5,'M','Black',60,12),
(5,'L','Gray',60,11),
(5,'L','Black',60,10),
(5,'XL','Gray',60,9),
(5,'XL','Black',60,8),
(5,'XXL','Gray',60,6),
(5,'XXL','Black',60,5),

(6,'S','Red',55,14),
(6,'S','Black',55,13),
(6,'M','Red',55,12),
(6,'M','Black',55,11),
(6,'L','Red',55,10),
(6,'L','Black',55,9),
(6,'XL','Red',55,8),
(6,'XL','Black',55,7),
(6,'XXL','Red',55,6),
(6,'XXL','Black',55,5),

(7,'S','Home',90,20),
(7,'S','Away',90,18),
(7,'M','Home',90,17),
(7,'M','Away',90,15),
(7,'L','Home',90,14),
(7,'L','Away',90,12),
(7,'XL','Home',90,10),
(7,'XL','Away',90,9),
(7,'XXL','Home',90,7),
(7,'XXL','Away',90,6),

(8,'S','Home',90,20),
(8,'S','Away',90,18),
(8,'M','Home',90,16),
(8,'M','Away',90,14),
(8,'L','Home',90,12),
(8,'L','Away',90,10),
(8,'XL','Home',90,9),
(8,'XL','Away',90,8),
(8,'XXL','Home',90,6),
(8,'XXL','Away',90,5),

(9,'Free','Black',50,30),
(9,'Free','Gray',50,28),
(9,'Free','Blue',50,26),
(9,'Free','Red',50,24),
(9,'Free','Green',50,22),
(9,'Free','White',50,20),
(9,'Free','Yellow',50,18),
(9,'Free','Pink',50,16),
(9,'Free','Camouflage',50,14),
(9,'Free','Navy',50,12),

(10,'Free','Black',55,28),
(10,'Free','Gray',55,26),
(10,'Free','Blue',55,24),
(10,'Free','Red',55,22),
(10,'Free','Green',55,20),
(10,'Free','White',55,18),
(10,'Free','Yellow',55,16),
(10,'Free','Pink',55,14),
(10,'Free','Camouflage',55,12),
(10,'Free','Navy',55,10),

(10,'S','Home',90,20),(10,'S','Away',90,18),
(10,'M','Home',90,17),(10,'M','Away',90,15),
(10,'L','Home',90,14),(10,'L','Away',90,12),
(10,'XL','Home',90,10),(10,'XL','Away',90,9),
(10,'XXL','Home',90,7),(10,'XXL','Away',90,6),

(11,'S','Home',85,20),(11,'S','Away',85,18),
(11,'M','Home',85,17),(11,'M','Away',85,15),
(11,'L','Home',85,14),(11,'L','Away',85,12),
(11,'XL','Home',85,10),(11,'XL','Away',85,9),
(11,'XXL','Home',85,7),(11,'XXL','Away',85,6),

(12,'S','Home',85,20),(12,'S','Away',85,18),
(12,'M','Home',85,17),(12,'M','Away',85,15),
(12,'L','Home',85,14),(12,'L','Away',85,12),
(12,'XL','Home',85,10),(12,'XL','Away',85,9),
(12,'XXL','Home',85,7),(12,'XXL','Away',85,6),

(13,'S','Home',88,20),(13,'S','Away',88,18),
(13,'M','Home',88,17),(13,'M','Away',88,15),
(13,'L','Home',88,14),(13,'L','Away',88,12),
(13,'XL','Home',88,10),(13,'XL','Away',88,9),
(13,'XXL','Home',88,7),(13,'XXL','Away',88,6),

(14,'S','Home',87,20),(14,'S','Away',87,18),
(14,'M','Home',87,17),(14,'M','Away',87,15),
(14,'L','Home',87,14),(14,'L','Away',87,12),
(14,'XL','Home',87,10),(14,'XL','Away',87,9),
(14,'XXL','Home',87,7),(14,'XXL','Away',87,6),

(15,'S','Home',89,20),(15,'S','Away',89,18),
(15,'M','Home',89,17),(15,'M','Away',89,15),
(15,'L','Home',89,14),(15,'L','Away',89,12),
(15,'XL','Home',89,10),(15,'XL','Away',89,9),
(15,'XXL','Home',89,7),(15,'XXL','Away',89,6),

(16,'S','Home',86,20),(16,'S','Away',86,18),
(16,'M','Home',86,17),(16,'M','Away',86,15),
(16,'L','Home',86,14),(16,'L','Away',86,12),
(16,'XL','Home',86,10),(16,'XL','Away',86,9),
(16,'XXL','Home',86,7),(16,'XXL','Away',86,6),

(17,'S','Home',75,25),(17,'S','Away',75,20),
(17,'M','Home',75,22),(17,'M','Away',75,18),
(17,'L','Home',75,20),(17,'L','Away',75,15),
(17,'XL','Home',75,12),(17,'XL','Away',75,10),
(17,'XXL','Home',75,8),(17,'XXL','Away',75,6),

(18,'S','Home',75,25),(18,'S','Away',75,20),
(18,'M','Home',75,22),(18,'M','Away',75,18),
(18,'L','Home',75,20),(18,'L','Away',75,15),
(18,'XL','Home',75,12),(18,'XL','Away',75,10),
(18,'XXL','Home',75,8),(18,'XXL','Away',75,6),

(19,'Free','Black',60,30),
(19,'Free','Gray',60,28),
(19,'Free','Blue',60,26),
(19,'Free','Red',60,24),
(19,'Free','Green',60,22),
(19,'Free','White',60,20),
(19,'Free','Yellow',60,18),
(19,'Free','Pink',60,16),
(19,'Free','Camouflage',60,14),
(19,'Free','Navy',60,12),


(20,'Free','Black',50,28),
(20,'Free','Gray',50,26),
(20,'Free','Blue',50,24),
(20,'Free','Red',50,22),
(20,'Free','Green',50,20),
(20,'Free','White',50,18),
(20,'Free','Yellow',50,16),
(20,'Free','Pink',50,14),
(20,'Free','Camouflage',50,12),
(20,'Free','Navy',50,10),

(21,'Free','Black',45,30),
(21,'Free','Gray',45,28),
(21,'Free','Blue',45,26),
(21,'Free','Red',45,24),
(21,'Free','Green',45,22),
(21,'Free','White',45,20),
(21,'Free','Yellow',45,18),
(21,'Free','Pink',45,16),
(21,'Free','Camouflage',45,14),
(21,'Free','Navy',45,12);

INSERT INTO Orders (UserID, TotalPrice, PaymentMethod, PaymentStatus, Status) VALUES
(3, 300.00, 'BANK', 'PAID', 'COMPLETED'),
(3, 175.00, 'COD', 'UNPAID', 'NEW');
GO

INSERT INTO OrderDetails (OrderID, VariantID, Quantity, UnitPrice) VALUES
-- Order 2
(2, 2, 2, 120.00),
(2, 6, 1, 35.00),

-- Order 3
(3, 4, 1, 150.00),
(3, 11, 1, 25.00);
GO

INSERT INTO Reviews (ProductID, UserID, Rating, Comment) VALUES
(1, 3, 5, N'Giày rất êm, chạy thích'),
(3, 3, 4, N'Form đẹp, hơi cứng lúc đầu'),
(9, 3, 5, N'Áo đẹp, đúng hàng chính hãng'),
(15, 3, 4, N'Balo rộng, đựng đồ thoải mái');
GO


-- Đánh giá sản phẩm
SELECT p.ProductName, r.Rating, r.Comment
FROM Reviews r
JOIN Products p ON r.ProductID = p.ProductID;

-- Danh sách sản phẩm + biến thể
SELECT p.ProductName, v.Size, v.Color, v.Price, v.Stock
FROM Products p
JOIN ProductVariants v ON p.ProductID = v.ProductID;

-- Lịch sử đơn hàng khách hàng
SELECT u.FullName, o.OrderID, o.TotalPrice, o.Status
FROM Orders o
JOIN Users u ON o.UserID = u.UserID;

-- DELETE FROM Categories;

ALTER TABLE Orders
ADD ShippingAddress NVARCHAR(255),
    PhoneNumber NVARCHAR(20)