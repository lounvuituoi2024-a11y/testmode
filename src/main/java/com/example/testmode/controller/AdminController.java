package com.example.testmode.controller;

import com.example.testmode.model.NguoiDung;
import com.example.testmode.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private NguoiDungService nguoiDungService;
    
    // Lấy danh sách tất cả người dùng với phân trang và tìm kiếm
    @GetMapping("/users")
    public ResponseEntity<Page<NguoiDung>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String quyen,
            @RequestParam(required = false) String trangThai) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<NguoiDung> users;
            if (keyword != null || quyen != null || trangThai != null) {
                users = nguoiDungService.searchUsers(keyword, quyen, trangThai, pageable);
            } else {
                users = nguoiDungService.getAllUsers(pageable);
            }
            
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Lấy thông tin chi tiết một người dùng
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<NguoiDung> userOpt = nguoiDungService.findById(id);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Tạo người dùng mới
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody NguoiDung nguoiDung) {
        try {
            nguoiDungService.createUser(nguoiDung);
            return ResponseEntity.ok("Thêm người dùng thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tạo người dùng thất bại: " + e.getMessage());
        }
    }
    
    // Cập nhật thông tin người dùng
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody NguoiDung nguoiDung) {
        try {
            Optional<NguoiDung> existingUser = nguoiDungService.findById(id);
            if (existingUser.isPresent()) {
                nguoiDung.setId(id);
                nguoiDungService.updateUser(id, nguoiDung);
                return ResponseEntity.ok("Cập nhật người dùng thành công");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật người dùng thất bại: " + e.getMessage());
        }
    }

    // Cập nhật quyền người dùng
    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Integer id,
            @RequestParam String quyen) {
        try {
            if (!quyen.matches("ADMIN|NHANVIEN|KHACHHANG")) {
                return ResponseEntity.badRequest().body("Quyền không hợp lệ");
            }
            
            nguoiDungService.updateUserRole(id, quyen);
            return ResponseEntity.ok("Cập nhật quyền thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật quyền thất bại: " + e.getMessage());
        }
    }
    
    // Cập nhật trạng thái người dùng (khóa/mở khóa)
    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Integer id,
            @RequestParam String trangThai) {
        try {
            if (!trangThai.matches("HOATDONG|KHOA")) {
                return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
            }
            
            nguoiDungService.updateUserStatus(id, trangThai);
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật trạng thái thất bại: " + e.getMessage());
        }
    }
    
    // Xóa người dùng
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            nguoiDungService.deleteUser(id);
            return ResponseEntity.ok("Xóa người dùng thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Xóa người dùng thất bại: " + e.getMessage());
        }
    }
}
