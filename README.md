# Hệ thống Quản lý Người dùng và Sản phẩm

## Mô tả
Dự án Spring Boot để quản lý người dùng và sản phẩm với các chức năng:
- Quản lý tài khoản (đăng nhập, đăng ký, phân quyền)
- Quản lý người dùng (CRUD, tìm kiếm, sắp xếp)
- Quản lý sản phẩm (CRUD, tìm kiếm, thống kê)

## Công nghệ sử dụng
- **Backend**: Spring Boot 3.5.5, Java 17
- **Database**: SQL Server
- **ORM**: Spring Data JPA
- **Validation**: Spring Boot Validation
- **API**: RESTful APIs

## Cấu trúc Project
```
src/
├── main/
│   ├── java/com/example/testmode/
│   │   ├── config/         # Cấu hình (CORS, Data Initializer)
│   │   ├── controller/     # REST Controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── model/         # Entity classes
│   │   ├── repository/    # Repository interfaces
│   │   ├── service/       # Service classes
│   │   └── TestmodeApplication.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
```

## Thiết lập Database

### 1. Tạo Database
```sql
CREATE DATABASE TESTMODE;
```

### 2. Tạo bảng NguoiDung
```sql
CREATE TABLE NguoiDung (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten_dang_nhap NVARCHAR(50) NOT NULL UNIQUE,
    mat_khau NVARCHAR(255) NOT NULL,
    ho_ten NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    so_dien_thoai NVARCHAR(20),
    dia_chi NVARCHAR(255),
    quyen NVARCHAR(20) NOT NULL,
    trang_thai NVARCHAR(20) NOT NULL
);
```

### 3. Tạo bảng SanPham
```sql
CREATE TABLE SanPham (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten NVARCHAR(100) NOT NULL,
    gia DECIMAL(18, 2) NOT NULL,
    so_luong INT NOT NULL,
    mo_ta NVARCHAR(500),
    trang_thai NVARCHAR(20)
);
```

## Cấu hình

### application.properties
Cập nhật thông tin kết nối database trong `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=TESTMODE;encrypt=true;trustServerCertificate=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

## Chạy ứng dụng

### 1. Compile project
```bash
mvn clean compile
```

### 2. Thiết lập OAuth2 (Tùy chọn)
Xem hướng dẫn chi tiết trong file [OAUTH2_SETUP_GUIDE.md](OAUTH2_SETUP_GUIDE.md)

#### Cấu hình nhanh OAuth2:
1. **Google**: Tạo OAuth2 credentials tại [Google Cloud Console](https://console.cloud.google.com/)
2. **Facebook**: Tạo app tại [Facebook for Developers](https://developers.facebook.com/)
3. Cập nhật `application.properties`:
```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET

# Facebook OAuth2
spring.security.oauth2.client.registration.facebook.client-id=YOUR_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=YOUR_FACEBOOK_APP_SECRET
```

### 3. Chạy ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

### 4. Truy cập ứng dụng
- **Web UI**: `http://localhost:8080/login`
- **API Test**: `http://localhost:8080/api/test`

## API Endpoints

### Quản lý Người dùng (/api/nguoi-dung)

#### Đăng nhập/Đăng ký
- `POST /api/nguoi-dung/dang-nhap` - Đăng nhập
- `POST /api/nguoi-dung/dang-ky` - Đăng ký khách hàng

#### CRUD Operations
- `GET /api/nguoi-dung` - Lấy tất cả người dùng
- `GET /api/nguoi-dung/{id}` - Lấy người dùng theo ID
- `POST /api/nguoi-dung` - Tạo người dùng mới (Admin)
- `PUT /api/nguoi-dung/{id}` - Cập nhật người dùng
- `DELETE /api/nguoi-dung/{id}` - Xóa người dùng (Admin)

#### Chức năng khác
- `GET /api/nguoi-dung/tim-kiem?keyword=...` - Tìm kiếm
- `GET /api/nguoi-dung/quyen/{quyen}` - Lọc theo quyền
- `GET /api/nguoi-dung/trang-thai/{trangThai}` - Lọc theo trạng thái
- `PUT /api/nguoi-dung/{id}/doi-mat-khau` - Đổi mật khẩu
- `PUT /api/nguoi-dung/{id}/toggle-trang-thai` - Khóa/mở khóa tài khoản

### Quản lý Sản phẩm (/api/san-pham)

#### CRUD Operations
- `GET /api/san-pham` - Lấy tất cả sản phẩm
- `GET /api/san-pham/{id}` - Lấy sản phẩm theo ID
- `POST /api/san-pham` - Tạo sản phẩm mới (Nhân viên)
- `PUT /api/san-pham/{id}` - Cập nhật sản phẩm (Nhân viên)
- `DELETE /api/san-pham/{id}` - Xóa sản phẩm (Nhân viên)

#### Tìm kiếm và Lọc
- `GET /api/san-pham/tim-kiem?keyword=...` - Tìm kiếm
- `GET /api/san-pham/trang-thai/{trangThai}` - Lọc theo trạng thái
- `GET /api/san-pham/khoang-gia?giaMin=...&giaMax=...` - Lọc theo giá

#### Thống kê
- `GET /api/san-pham/thong-ke/trang-thai` - Thống kê theo trạng thái
- `GET /api/san-pham/tong-gia-tri-kho` - Tổng giá trị kho
- `GET /api/san-pham/sap-het-hang` - Sản phẩm sắp hết hàng

### Test API
- `GET /api/test` - Kiểm tra API hoạt động
- `GET /api/health` - Kiểm tra trạng thái service

### OAuth2 APIs
- `GET /api/oauth2/current-user` - Lấy thông tin user OAuth2 hiện tại
- `GET /api/oauth2/status` - Kiểm tra trạng thái đăng nhập OAuth2
- `POST /api/oauth2/logout` - Đăng xuất OAuth2

### Web UI
- `GET /login` - Trang đăng nhập (OAuth2 + thông thường)
- `GET /home` - Trang chủ sau đăng nhập
- `GET /admin/dashboard` - Dashboard Admin
- `GET /employee/dashboard` - Dashboard Nhân viên  
- `GET /customer/dashboard` - Dashboard Khách hàng

## Dữ liệu mẫu

Hệ thống tự động tạo dữ liệu mẫu khi khởi động:

### Tài khoản mẫu:
1. **Admin**: `admin` / `admin123`
2. **Nhân viên**: `nhanvien01` / `nv123456`
3. **Khách hàng**: `khachhang01` / `kh123456`

### Sản phẩm mẫu:
- Laptop Dell Inspiron 15
- iPhone 15 Pro
- Samsung Galaxy S24
- MacBook Air M2
- AirPods Pro Gen 2
- iPad Pro 12.9 inch

## Quyền hạn

### ADMIN
- Quản lý toàn bộ người dùng (xem, thêm, xóa, phân quyền)
- Tìm kiếm, sắp xếp người dùng
- Thống kê hệ thống

### NHANVIEN
- Quản lý sản phẩm (CRUD)
- Xem, sửa thông tin cá nhân
- Đổi mật khẩu

### KHACHHANG
- Xem, cập nhật thông tin cá nhân
- Đổi mật khẩu

## Validation

Hệ thống có validation cho:
- Email đúng định dạng
- Tên đăng nhập duy nhất
- Email duy nhất
- Mật khẩu tối thiểu 6 ký tự
- Các trường bắt buộc không được để trống
- Giá sản phẩm > 0
- Số lượng sản phẩm >= 0

## Test với Postman

### 1. Test đăng nhập
```json
POST http://localhost:8080/api/nguoi-dung/dang-nhap
Content-Type: application/json

{
    "tenDangNhap": "admin",
    "matKhau": "admin123"
}
```

### 2. Test tạo sản phẩm
```json
POST http://localhost:8080/api/san-pham
Content-Type: application/json

{
    "ten": "Sản phẩm test",
    "gia": 1000000,
    "soLuong": 50,
    "moTa": "Mô tả sản phẩm test",
    "trangThai": "CONHANG"
}
```

## Lưu ý
- Đảm bảo SQL Server đang chạy
- Cập nhật thông tin kết nối database trong application.properties
- Port mặc định: 8080
- CORS đã được cấu hình cho phép all origins (*)
