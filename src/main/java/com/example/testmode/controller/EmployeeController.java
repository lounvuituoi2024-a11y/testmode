package com.example.testmode.controller;

import com.example.testmode.dto.ApiResponse;
import com.example.testmode.dto.ProductStatsResponse;
import com.example.testmode.model.SanPham;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@PreAuthorize("hasRole('NHANVIEN')")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private SanPhamService sanPhamService;
    
    // Lấy danh sách tất cả sản phẩm với phân trang và tìm kiếm
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<SanPham>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<SanPham> products;
            if (keyword != null || trangThai != null) {
                products = sanPhamService.searchProducts(keyword, trangThai, pageable);
            } else {
                products = sanPhamService.getAllProducts(pageable);
            }
            
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
    
    // Lấy thông tin chi tiết một sản phẩm
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<SanPham>> getProductById(@PathVariable Integer id) {
        try {
            Optional<SanPham> productOpt = sanPhamService.findById(id);
            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy sản phẩm"));
            }
            return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sản phẩm thành công", productOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
    
    // Tạo sản phẩm mới
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<SanPham>> createProduct(@Valid @RequestBody SanPham sanPham) {
        try {
            SanPham newProduct = sanPhamService.createProduct(sanPham);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo sản phẩm thành công", newProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Tạo sản phẩm thất bại: " + e.getMessage()));
        }
    }
    
    // Cập nhật sản phẩm
    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<SanPham>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody SanPham sanPham) {
        try {
            SanPham updatedProduct = sanPhamService.updateProduct(id, sanPham);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Cập nhật sản phẩm thất bại: " + e.getMessage()));
        }
    }
    
    // Cập nhật trạng thái sản phẩm
    @PutMapping("/products/{id}/status")
    public ResponseEntity<ApiResponse<SanPham>> updateProductStatus(
            @PathVariable Integer id,
            @RequestParam String trangThai) {
        try {
            if (!trangThai.matches("CONHANG|HETHANG|NGUNGBAN")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Trạng thái không hợp lệ. Chỉ chấp nhận: CONHANG, HETHANG, NGUNGBAN"));
            }
            
            SanPham updatedProduct = sanPhamService.updateProductStatus(id, trangThai);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái sản phẩm thành công", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Cập nhật trạng thái thất bại: " + e.getMessage()));
        }
    }
    
    // Cập nhật số lượng sản phẩm
    @PutMapping("/products/{id}/quantity")
    public ResponseEntity<ApiResponse<SanPham>> updateProductQuantity(
            @PathVariable Integer id,
            @RequestParam Integer soLuong) {
        try {
            if (soLuong < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Số lượng không được âm"));
            }
            
            SanPham updatedProduct = sanPhamService.updateProductQuantity(id, soLuong);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật số lượng sản phẩm thành công", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Cập nhật số lượng thất bại: " + e.getMessage()));
        }
    }
    
    // Xóa sản phẩm
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Integer id) {
        try {
            sanPhamService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Xóa sản phẩm thất bại: " + e.getMessage()));
        }
    }
    
    // Lấy sản phẩm sắp hết hàng
    @GetMapping("/products/low-stock")
    public ResponseEntity<ApiResponse<List<SanPham>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {
        try {
            List<SanPham> lowStockProducts = sanPhamService.getLowStockProducts(threshold);
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm sắp hết hàng thành công", lowStockProducts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
    
    // Thống kê sản phẩm theo trạng thái
    @GetMapping("/products/stats/status")
    public ResponseEntity<ApiResponse<ProductStatsResponse>> getProductStatsByStatus() {
        try {
            long conhangCount = sanPhamService.countProductsByStatus("CONHANG");
            long hethangCount = sanPhamService.countProductsByStatus("HETHANG");
            long ngungbanCount = sanPhamService.countProductsByStatus("NGUNGBAN");
            
            ProductStatsResponse stats = new ProductStatsResponse(conhangCount, hethangCount, ngungbanCount);
            return ResponseEntity.ok(ApiResponse.success("Thống kê sản phẩm theo trạng thái", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Lỗi server: " + e.getMessage()));
        }
    }
}
