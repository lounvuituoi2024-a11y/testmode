package com.example.testmode.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class để encode password thành BCrypt hash
 * Chạy class này để lấy BCrypt hash của password
 */
public class PasswordEncoder {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Encode các password
        String admin123 = encoder.encode("admin123");
        String nv123456 = encoder.encode("nv123456");
        String kh123456 = encoder.encode("kh123456");
        
        System.out.println("BCrypt hashes:");
        System.out.println("admin123: " + admin123);
        System.out.println("nv123456: " + nv123456);
        System.out.println("kh123456: " + kh123456);
        
        // Tạo SQL update statements
        System.out.println("\nSQL Update statements:");
        System.out.println("UPDATE NguoiDung SET mat_khau = '" + admin123 + "' WHERE ten_dang_nhap = 'admin';");
        System.out.println("UPDATE NguoiDung SET mat_khau = '" + nv123456 + "' WHERE ten_dang_nhap = 'nhanvien01';");
        System.out.println("UPDATE NguoiDung SET mat_khau = '" + kh123456 + "' WHERE ten_dang_nhap = 'khachhang01';");
    }
}
