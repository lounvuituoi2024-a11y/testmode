# Hướng dẫn thiết lập OAuth2 với Google và Facebook

## 1. Thiết lập Google OAuth2

### Bước 1: Tạo Google Cloud Project
1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project hiện có
3. Vào "APIs & Services" > "Credentials"

### Bước 2: Tạo OAuth 2.0 Credentials
1. Click "Create Credentials" > "OAuth client ID"
2. Chọn "Web application"
3. Đặt tên cho client ID
4. Thêm "Authorized redirect URIs":
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
5. Lưu và copy **Client ID** và **Client Secret**

### Bước 3: Cấu hình application.properties
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

---

## 2. Thiết lập Facebook OAuth2

### Bước 1: Tạo Facebook App
1. Truy cập [Facebook for Developers](https://developers.facebook.com/)
2. Click "My Apps" > "Create App"
3. Chọn "Consumer" và điền thông tin app

### Bước 2: Thiết lập Facebook Login
1. Vào app dashboard > "Add Product" > "Facebook Login"
2. Chọn "Web" platform
3. Vào "Facebook Login" > "Settings"
4. Thêm "Valid OAuth Redirect URIs":
   ```
   http://localhost:8080/login/oauth2/code/facebook
   ```

### Bước 3: Lấy App ID và App Secret
1. Vào "Settings" > "Basic"
2. Copy **App ID** và **App Secret**

### Bước 4: Cấu hình application.properties
```properties
spring.security.oauth2.client.registration.facebook.client-id=YOUR_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=YOUR_FACEBOOK_APP_SECRET
```

---

## 3. Cấu hình hoàn chỉnh trong application.properties

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,email,profile

# Facebook OAuth2
spring.security.oauth2.client.registration.facebook.client-id=YOUR_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=YOUR_FACEBOOK_APP_SECRET
spring.security.oauth2.client.registration.facebook.scope=email,public_profile

# OAuth2 Provider Configuration (Optional - Spring Boot auto-configures these)
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/v2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v2/userinfo
spring.security.oauth2.client.provider.google.user-name-attribute=id

spring.security.oauth2.client.provider.facebook.authorization-uri=https://www.facebook.com/v12.0/dialog/oauth
spring.security.oauth2.client.provider.facebook.token-uri=https://graph.facebook.com/v12.0/oauth/access_token
spring.security.oauth2.client.provider.facebook.user-info-uri=https://graph.facebook.com/v12.0/me?fields=id,name,email
spring.security.oauth2.client.provider.facebook.user-name-attribute=id
```

---

## 4. Cách sử dụng

### Đăng nhập qua Web UI
1. Truy cập: `http://localhost:8080/login`
2. Click nút "Đăng nhập với Google" hoặc "Đăng nhập với Facebook"
3. Hoàn tất quy trình OAuth2
4. Hệ thống sẽ tự động tạo tài khoản nếu email chưa tồn tại

### API Endpoints cho OAuth2
- `GET /api/oauth2/current-user` - Lấy thông tin user hiện tại
- `GET /api/oauth2/status` - Kiểm tra trạng thái đăng nhập
- `POST /api/oauth2/logout` - Đăng xuất

### Luồng xử lý OAuth2
1. User click đăng nhập OAuth2
2. Redirect đến provider (Google/Facebook)
3. User authorize app
4. Callback về `/login/oauth2/code/{provider}`
5. `CustomOAuth2UserService` xử lý thông tin user
6. Tự động tạo account nếu chưa có
7. `OAuth2LoginSuccessHandler` redirect theo role
8. User vào dashboard tương ứng

---

## 5. Lưu ý quan trọng

### Security
- Không commit Client Secret vào Git
- Sử dụng environment variables cho production
- Cấu hình HTTPS cho production

### Development vs Production
- **Development**: `http://localhost:8080`
- **Production**: Cập nhật redirect URIs với domain thật

### Quyền OAuth2
- **Google**: Cần scope `openid,email,profile`
- **Facebook**: Cần scope `email,public_profile`
- Facebook có thể cần review app cho scope `email`

---

## 6. Troubleshooting

### Lỗi thường gặp
1. **Invalid redirect URI**: Kiểm tra URL trong OAuth2 settings
2. **Invalid client**: Kiểm tra Client ID/Secret
3. **Scope not granted**: Kiểm tra permissions của app

### Debug
- Bật log: `logging.level.org.springframework.security=DEBUG`
- Kiểm tra network tab trong browser
- Xem logs Spring Boot console

---

## 7. Example sử dụng

### Test OAuth2 Login
1. Start ứng dụng: `mvn spring-boot:run`
2. Mở browser: `http://localhost:8080/login`
3. Click "Đăng nhập với Google"
4. Kiểm tra được redirect về home page với thông tin user

### Test API
```bash
# Lấy thông tin user (cần đăng nhập trước)
curl -X GET http://localhost:8080/api/oauth2/current-user \
  -H "Cookie: JSESSIONID=YOUR_SESSION_ID"
```
