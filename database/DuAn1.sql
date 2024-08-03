
DROP DATABASE TECHZONE
CREATE DATABASE TECHZONE
 use TECHZONE

CREATE TABLE LOAIPK
(
	ID_LPK int IDENTITY(1,1) PRIMARY KEY,
	TENLPK NVARCHAR(50) NOT NULL,
    MOTA NVARCHAR(100)
)

CREATE TABLE HANGSX
(
    ID_HANGSX int IDENTITY(1,1) PRIMARY KEY,
	TENHANGSX NVARCHAR(50) NOT NULL,
    MOTA NVARCHAR(100)
)

CREATE TABLE SANPHAM
(
    ID_SP int IDENTITY(1,1) PRIMARY KEY,
	TENSP NVARCHAR(50) NOT NULL,
	MALOAIPK INT NOT NULL,
	MAHANGSX INT NOT NULL,
	SOLUONG INT NOT NULL,
	GIABAN FLOAT NOT NULL,
	HINH NVARCHAR(50) NOT NULL,
	MOTA NVARCHAR(100) NOT NULL
    FOREIGN KEY (MALOAIPK) REFERENCES LOAIPK (ID_LPK) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (MAHANGSX) REFERENCES HANGSX (ID_HANGSX) ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE CHUCVU
(
	ID_CV INT IDENTITY(1,1) PRIMARY KEY,
	TENCV NVARCHAR(50) NOT NULL,
	MOTA NVARCHAR(100) NOT NULL
)

CREATE TABLE TAIKHOAN
(
	ID_TK INT IDENTITY(1,1) PRIMARY KEY,
	MACV INT NOT NULL,
	TENDN NVARCHAR(20) NOT NULL,
	TENNV NVARCHAR(30) NOT NULL,
	EMAIL NVARCHAR(50) NOT NULL,
	MATKHAU NVARCHAR(30) NOT NULL,
	DIACHI NVARCHAR(50) NOT NULL,
	DIENTHOAI NVARCHAR(10) NOT NULL,
	NGAYSINH DATE NOT NULL,
	GIOITINH BIT NOT NULL,
	TRANGTHAI BIT NOT NULL,
    FOREIGN KEY (MACV) REFERENCES CHUCVU (ID_CV) ON DELETE CASCADE ON UPDATE CASCADE
)
CREATE TABLE HOADON
(
    [ID_HD] [int] IDENTITY(1,1) NOT NULL,
	[MATAIKHOAN] [int] NOT NULL,
	[NGAYLAP] [date] NOT NULL,
	[TONGTIEN] [float] NOT NULL,
	[PHANTRAMGG] [int] NULL,
	[THANHTIEN] [float] NOT NULL,
	[TRANGTHAI] [int] NULL,
	[TenKH] [nvarchar](30) NULL,
	[SDT] [nvarchar](10) NULL,
	[DiaChi] [nvarchar](30) NULL,
	[HinhThucTT] [bit] NOT NULL,
	[LyDo] [nvarchar](30) NULL,
    FOREIGN KEY (MATAIKHOAN) REFERENCES TAIKHOAN (ID_TK) ON DELETE CASCADE ON UPDATE CASCADE
)
ALTER TABLE HOADON
ADD CONSTRAINT PK_HOADON PRIMARY KEY (ID_HD);
CREATE TABLE CHITIETHOADON
(
    ID_HDCT int IDENTITY(1,1) PRIMARY KEY,
	MAHD INT NOT NULL,
	MASP INT NOT NULL,
	NGAYLAP DATE NOT NULL,
	GIABAN FLOAT NOT NULL,
	SOLUONG INT NOT NULL,
    FOREIGN KEY (MAHD) REFERENCES HOADON (ID_HD) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (MASP) REFERENCES SANPHAM (ID_SP) ON DELETE CASCADE ON UPDATE CASCADE
)
----------------------------------------------------------------------------------------
-- Chèn dữ liệu vào bảng LOAIPK
INSERT INTO LOAIPK (TENLPK, MOTA) VALUES
(N'Dây cáp', N'Cáp USB-C to Type-C Apple MQKJ3ZA/A dây dù 1m'),
(N'Dây cáp', N'Cáp Apple USB-C to Type-C dây dù 240W 2M MU2G3ZA/A'),
(N'Dây cáp', N'Cáp Aukey USB-A to USB-C dây dù 0.9 mét CB-CD30'),
(N'Ốp điện thoại', N'Ốp lưng iPhone 13 Pro Jinya Crystal'),
(N'Ốp điện thoại', N'Ốp lưng iPhone 13 Pro Raptic Terrian'),
(N'Ốp điện thoại', N'Ốp lưng iPhone 13 Pro OU Trong suốt'),
(N'Sạc dự phòng', N'Pin sạc dự phòng Baseus Airpow Fast Charge 20000mAh 20W'),
(N'Sạc dự phòng', N'Pin sạc dự phòng Energizer 20000mAh QE20013PQ'),
(N'Sạc dự phòng', N'Pin sạc dự phòng Anker 325 Powercore II 1C1A 15W 20000mAh A1286'),
(N'Tai nghe', N'Tai nghe Bluetooth True Wireless HUAWEI FreeClip'),
(N'Tai nghe', N'Tai nghe Bluetooth True Wireless Havit TW969'),
(N'Tai nghe', N'Tai nghe Bluetooth Apple AirPods Pro 2 2023 USB-C')

-- Chèn dữ liệu vào bảng HANGSX
INSERT INTO HANGSX (TENHANGSX, MOTA) VALUES
(N'Apple', N'Thương hiệu Apple'),
(N'Aukey', N'Thương hiệu Aukey'),
(N'Jinya', N'Thương hiệu Jinya'),
(N'Raptic', N'Thương hiệu Raptic'),
(N'OU', N'Thương hiệu OU'),
(N'Baseus', N'Thương hiệu Baseus'),
(N'Energizer', N'Thương hiệu Energizer'),
(N'Anker', N'Thương hiệu Anker'),
(N'HUAWEI', N'Thương hiệu HUAWEI'),
(N'Havit', N'Thương hiệu Havit');

-- Chèn dữ liệu vào bảng SANPHAM
INSERT INTO SANPHAM (TENSP, MALOAIPK, MAHANGSX, SOLUONG, GIABAN, HINH, MOTA) VALUES
(N'Cáp USB-C to Type-C', 1, 1, 100, 540000, N'DayCap1.jpg', N'Cáp USB-C to Type-C Apple MQKJ3ZA/A dây dù 1m'),
(N'Cáp Apple USB-C to Type-C', 2, 1, 50, 720000, N'DayCap2.jpg', N'Cáp Apple USB-C to Type-C dây dù 240W 2M MU2G3ZA/A'),
(N'Cáp Aukey USB-A to USB-C', 3, 2, 75, 110000, N'DayCap3.jpg', N'Cáp Aukey USB-A to USB-C dây dù 0.9 mét CB-CD30'),
(N'Ốp lưng iPhone 13 Pro Jinya Crystal', 4, 3, 80, 99000, N'Op3.jpg', N'Ốp lưng iPhone 13 Pro Jinya Crystal'),
(N'Ốp lưng iPhone 13 Pro Raptic Terrian', 5, 4, 40, 70000, N'Op2.jpg', N'Ốp lưng iPhone 13 Pro Raptic Terrian'),
(N'Ốp lưng iPhone 13 Pro OU Trong suốt', 6, 5, 60, 59000, N'Op1.jpg', N'Ốp lưng iPhone 13 Pro OU Trong suốt'),
(N'Pin sạc dự phòng Baseus', 7, 6, 90, 420000, N'Sac1.jpg', N'Pin sạc dự phòng Baseus Airpow Fast Charge 20000mAh 20W'),
(N'Pin sạc dự phòng Energizer', 8, 7, 120, 840000, N'Sac2.jpg', N'Pin sạc dự phòng Energizer 20000mAh QE20013PQ'),
(N'Pin sạc dự phòng Anker', 9, 8, 110, 650000, N'Sac3.jpg', N'Pin sạc dự phòng Anker 325 Powercore II 1C1A 15W 20000mAh A1286'),
(N'Tai nghe Bluetooth HUAWEI', 10, 9, 130, 100000, N'TaiNghe1.jpg', N'Tai nghe Bluetooth True Wireless HUAWEI FreeClip'),
(N'Tai nghe Bluetooth Havit', 11, 10, 130, 100000, N'TaiNghe2.jpg', N'Tai nghe Bluetooth True Wireless Havit TW969'),
(N'Tai nghe Bluetooth Apple', 12, 1, 130, 100000, N'TaiNghe3.jpg', N'Tai nghe Bluetooth Apple AirPods Pro 2 2023 USB-C');

-- Chèn dữ liệu vào bảng CHUCVU
INSERT INTO CHUCVU (TENCV, MOTA) VALUES
(N'Nhân viên', N'Nhân viên bán hàng'),
(N'Quản lý', N'Quản lý cửa hàng'),
(N'Admin', N'Quản trị hệ thống');

-- Chèn dữ liệu vào bảng TAIKHOAN
INSERT INTO TAIKHOAN (MACV, TENDN, TENNV, EMAIL, MATKHAU, DIACHI, DIENTHOAI, NGAYSINH, GIOITINH, TRANGTHAI) VALUES
(1, N'nhanvien01', N'Nguyễn Thanh Tùng', N'nguyenthanhtung@email.com', N'matkhau01', N'Số 10, Đường Trần Hưng Đạo, Quận 1, HCM', N'0901234567', '1990-01-01', 1, 1),
(1, N'nhanvien02', N'Trần Minh Huy', N'tranminhhuy@email.com', N'matkhau02', N'Số 25, Đường Nguyễn Thị Minh Khai, Quận 1, HCM', N'0912345678', '1991-02-02', 1, 1),
(1, N'nhanvien03', N'Lê Thị Ngọc Ánh', N'lengocanh@email.com', N'matkhau03', N'Số 18, Ngõ 121, Phố Chùa Láng, Hà Nội', N'0923456789', '1992-03-03', 0, 1),
(2, N'quanly01', N'Phạm Văn Đạt', N'phamvandat@email.com', N'matkhau04', N'Số 5, Đường Lê Duẩn, Quận 1, HCM', N'0934567890', '1985-04-04', 1, 1),
(2, N'quanly02', N'Hoàng Thị Ngọc Lan', N'hoangthi-ngoclan@email.com', N'matkhau05', N'Số 30, Đường Hàng Bông, Hà Nội', N'0945678901', '1986-05-05', 0, 1),
(2, N'quanly03', N'Nguyễn Văn Quang', N'nguyenvanquang@email.com', N'matkhau06', N'Số 8, Đường Nguyễn Huệ, Quận 1, HCM', N'0956789012', '1987-06-06', 1, 1),
(3, N'admin01', N'Trần Thị Thu Hương', N'tranthithuhuong@email.com', N'matkhau07', N'Số 45, Đường Tôn Đức Thắng, Quận 1, HCM', N'0967890123', '1980-07-07', 0, 1),
(3, N'admin02', N'Lê Văn Đức Anh', N'levanducanh@email.com', N'matkhau08', N'Số 12, Đường Lê Lợi, Quận 1, HCM', N'0978901234', '1981-08-08', 1, 1),
(3, N'admin03', N'Phạm Thị Hải Yến', N'phamthihaiyen@email.com', N'matkhau09', N'Số 22, Ngõ 24, Phố Trần Phú, Hà Nội', N'0989012345', '1982-09-09', 0, 1),
(3, N'admin04', N'Hoàng Văn Nam', N'hoangvannam@email.com', N'matkhau10', N'Số 3, Đường Lê Thánh Tông, Hà Nội', N'0990123456', '1983-10-10', 1, 1);

-- Chèn dữ liệu vào bảng HOADON
INSERT INTO HOADON (MATAIKHOAN, NGAYLAP, TONGTIEN, PHANTRAMGG, THANHTIEN, TRANGTHAI, TenKH, SDT, DiaChi, HinhThucTT, LyDo) VALUES
(1, '2024-07-12', 190000, 5, 171000, 1, 'Khach Hang 1', '0123456789', 'Dia Chi 1', 1, 'Ly Do 1'),
(2, '2024-07-11', 400000, 10, 320000, 1, 'Khach Hang 2', '0987654321', 'Dia Chi 2', 1, 'Ly Do 2'),
(3, '2024-07-10', 225000, 8, 191250, 1, 'Khach Hang 3', '0234567890', 'Dia Chi 3', 1, 'Ly Do 3'),
(4, '2024-07-09', 360000, 12, 270000, 1, 'Khach Hang 4', '0345678901', 'Dia Chi 4', 1, 'Ly Do 4'),
(5, '2024-07-08', 480000, 15, 336000, 1, 'Khach Hang 5', '0456789012', 'Dia Chi 5', 1, 'Ly Do 5'),
(6, '2024-07-07', 300000, 10, 240000, 1, 'Khach Hang 6', '0567890123', 'Dia Chi 6', 1, 'Ly Do 6'),
(7, '2024-07-06', 420000, 12, 315000, 1, 'Khach Hang 7', '0678901234', 'Dia Chi 7', 1, 'Ly Do 7'),
(8, '2024-07-05', 180000, 5, 162000, 1, 'Khach Hang 8', '0789012345', 'Dia Chi 8', 1, 'Ly Do 8'),
(9, '2024-07-04', 240000, 8, 204000, 1, 'Khach Hang 9', '0890123456', 'Dia Chi 9', 1, 'Ly Do 9'),
(10, '2024-07-03', 150000, 5, 135000, 1, 'Khach Hang 10', '0901234567', 'Dia Chi 10', 1, 'Ly Do 10');

-- Chèn dữ liệu vào bảng CHITIETHOADON
INSERT INTO CHITIETHOADON (MAHD, MASP, NGAYLAP, GIABAN, SOLUONG) VALUES
(1, 1, '2024-07-12', 200000, 1),
(2, 2, '2024-07-11', 500000, 1),
(3, 3, '2024-07-10', 300000, 1),
(4, 4, '2024-07-09', 400000, 1),
(5, 5, '2024-07-08', 800000, 1),
(6, 6, '2024-07-07', 600000, 1),
(7, 7, '2024-07-06', 450000, 1),
(8, 8, '2024-07-05', 250000, 1),
(9, 9, '2024-07-04', 150000, 1),
(10, 10, '2024-07-03', 100000, 1);
GO


----------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------
--Thống kê doanh thu theo ngày, tháng, năm
CREATE PROC sp_ThongKeDoanhThu_DMY
	(@Day INT,
	@Month INT,
	@Year INT)
AS
BEGIN
	SELECT
		HD.NGAYLAP,
		SUM(HD.THANHTIEN) TONGTIENHOMNAY
	FROM HOADON HD
	WHERE DAY(NGAYLAP) = @Day AND MONTH(NGAYLAP) = @Month AND YEAR(NGAYLAP) = @Year
	GROUP BY HD.NGAYLAP
END
GO
EXEC sp_ThongKeDoanhThu_DMY 7,7,2024
GO
--Thống kê doanh thu theo tháng, năm
CREATE PROC sp_ThongKeDoanhThu_MY
	(@Month INT,
	@Year INT)
AS
BEGIN
	SELECT
		MONTH(NGAYLAP) THANG,
		SUM(HD.THANHTIEN) TONGTIEN
	FROM HOADON HD
	WHERE MONTH(NGAYLAP) = @Month AND YEAR(NGAYLAP) = @Year
	GROUP BY MONTH(NGAYLAP)
END
GO
EXEC sp_ThongKeDoanhThu_MY 7,2024
GO
--Thống kê doanh thu theo năm
CREATE PROC sp_ThongKeDoanhThu_Y
	(@Year INT)
AS
BEGIN
	SELECT
		YEAR(NGAYLAP) NAM,
		SUM(HD.THANHTIEN) TONGTIEN
	FROM HOADON HD
	WHERE YEAR(NGAYLAP) = @Year
	GROUP BY YEAR(NGAYLAP)
END
GO
EXEC sp_ThongKeDoanhThu_Y 2024
GO
--Thống kê sản phẩm bán chạy theo ngày, tháng, năm
CREATE PROC sp_ThongKeSanPham_DMY
	(@Day int,
	@Month int,
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE DAY(HD.NGAYLAP) = @Day AND MONTH(HD.NGAYLAP) = @Month AND YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN DESC
END
GO
EXEC sp_ThongKeSanPham_DMY 7,7,2024
GO
--Thống kê sản phẩm bán chạy theo tháng, năm
CREATE PROC sp_ThongKeSanPham_MY
	(@Month int,
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE MONTH(HD.NGAYLAP) = @Month AND YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN DESC
END
GO
EXEC sp_ThongKeSanPham_MY 7,2024
GO
--Thống kê sản phẩm bán chạy theo năm
CREATE PROC sp_ThongKeSanPham_Y
	(
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN DESC
END
GO
EXEC sp_ThongKeSanPham_Y 2024
GO

--Thống kê sản phẩm theo loại
CREATE PROC sp_ThongKeSanPham_LOAIPK_Y
	(@Year int)
AS
BEGIN
	SELECT
		LH.TENLPK,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
		JOIN LOAIPK LH ON LH.ID_LPK = SP.MALOAIPK
	WHERE YEAR(HD.NGAYLAP) = @Year
	GROUP BY LH.TENLPK
	ORDER BY LUOTBAN DESC
END
GO
EXEC sp_ThongKeSanPham_LOAIPK_Y 2024
Go
-------------------------------------
--Thống kê sản phẩm bán chạy theo ngày, tháng, năm TĂNG DẦN
CREATE PROC sp_ThongKeSanPham_DMY_ASC
	(@Day int,
	@Month int,
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE DAY(HD.NGAYLAP) = @Day AND MONTH(HD.NGAYLAP) = @Month AND YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN ASC
END
GO
EXEC sp_ThongKeSanPham_DMY_ASC 7,7,2024
GO
--Thống kê sản phẩm bán chạy theo tháng, năm TĂNG DẦN
CREATE PROC sp_ThongKeSanPham_MY_ASC
	(@Month int,
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE MONTH(HD.NGAYLAP) = @Month AND YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN ASC
END
GO
EXEC sp_ThongKeSanPham_MY_ASC 7,2024
GO
--Thống kê sản phẩm bán chạy theo năm TĂNG DẦN
CREATE PROC sp_ThongKeSanPham_Y_ASC
	(
	@Year int)
AS
BEGIN
	SELECT
		SP.ID_SP,
		SP.TENSP,
		COUNT(HD.ID_HD) LUOTBAN
	FROM HOADON HD
		JOIN CHITIETHOADON CTHD ON HD.ID_HD = CTHD.MAHD
		JOIN SANPHAM SP ON SP.ID_SP = CTHD.MASP
	WHERE YEAR(HD.NGAYLAP) = @Year
	GROUP BY SP.ID_SP, SP.TENSP
	ORDER BY LUOTBAN ASC
END
GO
EXEC sp_ThongKeSanPham_Y_ASC 2024
GO
