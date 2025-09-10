package com.example.testmode.controller;

import com.example.testmode.dto.ApiResponse;
import com.example.testmode.dto.ChangePasswordRequest;
import com.example.testmode.dto.UpdateUserRequest;
import com.example.testmode.model.NguoiDung;
import com.example.testmode.model.SanPham;
import com.example.testmode.service.NguoiDungService;
import com.example.testmode.service.SanPhamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('KHACHHANG')")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private NguoiDungService nguoiDungService;
    
    @Autowired
    private SanPhamService sanPhamService;
    
    // Lấy thông tin cá nhân
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<NguoiDung>> getProfile(Principal principal) {
        try {
            String username = principal.getName();
            Optional<NguoiDung> userOpt = nguoiDungService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy thông tin người dùng"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Lấy thông tin cá nhân thành công", userOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
    
    // Cập nhật thông tin cá nhân
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<NguoiDung>> updateProfile(
            @Valid @RequestBody UpdateUserRequest updateRequest,
            Principal principal) {
        try {
            String username = principal.getName();
            Optional<NguoiDung> userOpt = nguoiDungService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy thông tin người dùng"));
            }
            
            NguoiDung updatedUser = nguoiDungService.updateUser(userOpt.get().getId(), updateRequest);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin thành công", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Cập nhật thất bại: " + e.getMessage()));
        }
    }
    
    // Thay đổi mật khẩu
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            Principal principal) {
        try {
            System.out.println("Change password request: " + principal.getName());
            System.out.println("Request data - Current password: " + (changePasswordRequest.getMatKhauHienTai() != null ? "***" : "null"));
            System.out.println("Request data - New password: " + (changePasswordRequest.getMatKhauMoi() != null ? "***" : "null"));
            System.out.println("Request data - Confirm password: " + (changePasswordRequest.getXacNhanMatKhau() != null ? "***" : "null"));
            
            String username = principal.getName();
            Optional<NguoiDung> userOpt = nguoiDungService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy thông tin người dùng"));
            }
            
            nguoiDungService.changePassword(userOpt.get().getId(), changePasswordRequest);
            return ResponseEntity.ok(ApiResponse.success("Thay đổi mật khẩu thành công"));
        } catch (Exception e) {
            System.out.println("Change password error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Thay đổi mật khẩu thất bại: " + e.getMessage()));
        }
    }
    
    // Xem danh sách sản phẩm (chỉ những sản phẩm đang hoạt động)
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<SanPham>>> getAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            // Chỉ hiển thị sản phẩm còn hàng cho khách hàng
            Page<SanPham> products = sanPhamService.searchProducts(keyword, "CONHANG", pageable);
            
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
    
    // Xem chi tiết sản phẩm
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<SanPham>> getProductDetail(@PathVariable Integer id) {
        try {
            Optional<SanPham> productOpt = sanPhamService.findById(id);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy sản phẩm"));
            }
            
            SanPham product = productOpt.get();
            // Kiểm tra sản phẩm có còn hàng không
            if (!"CONHANG".equals(product.getTrangThai())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Sản phẩm hiện không có sẵn"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sản phẩm thành công", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
}
