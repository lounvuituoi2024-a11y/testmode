package com.example.testmode.service;

import com.example.testmode.model.SanPham;
import com.example.testmode.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamService {
    
    @Autowired
    private SanPhamRepository sanPhamRepository;
    
    // Lấy tất cả sản phẩm với phân trang
    public Page<SanPham> getAllProducts(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }
    
    // Tìm kiếm sản phẩm
    public Page<SanPham> searchProducts(String keyword, String trangThai, Pageable pageable) {
        if (keyword == null) keyword = "";
        
        if (trangThai != null && !trangThai.isEmpty()) {
            return sanPhamRepository.findByTenContainingAndTrangThai(keyword, trangThai, pageable);
        } else {
            return sanPhamRepository.findByKeyword(keyword, pageable);
        }
    }
    
    // Tìm sản phẩm theo ID
    public Optional<SanPham> findById(Integer id) {
        return sanPhamRepository.findById(id);
    }
    
    // Tạo sản phẩm mới
    public SanPham createProduct(SanPham sanPham) {
        // Tự động set trạng thái dựa trên số lượng
        if (sanPham.getSoLuong() == null || sanPham.getSoLuong() <= 0) {
            sanPham.setTrangThai("HETHANG");
        } else {
            sanPham.setTrangThai("CONHANG");
        }
        
        return sanPhamRepository.save(sanPham);
    }
    
    // Cập nhật sản phẩm
    public SanPham updateProduct(Integer id, SanPham sanPhamUpdate) {
        SanPham existingProduct = sanPhamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        
        existingProduct.setTen(sanPhamUpdate.getTen());
        existingProduct.setGia(sanPhamUpdate.getGia());
        existingProduct.setSoLuong(sanPhamUpdate.getSoLuong());
        existingProduct.setMoTa(sanPhamUpdate.getMoTa());
        
        // Tự động cập nhật trạng thái dựa trên số lượng
        if (sanPhamUpdate.getSoLuong() == null || sanPhamUpdate.getSoLuong() <= 0) {
            existingProduct.setTrangThai("HETHANG");
        } else if ("HETHANG".equals(existingProduct.getTrangThai()) && sanPhamUpdate.getSoLuong() > 0) {
            existingProduct.setTrangThai("CONHANG");
        }
        
        return sanPhamRepository.save(existingProduct);
    }
    
    // Cập nhật trạng thái sản phẩm
    public SanPham updateProductStatus(Integer id, String trangThai) {
        SanPham product = sanPhamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        
        product.setTrangThai(trangThai);
        return sanPhamRepository.save(product);
    }
    
    // Xóa sản phẩm
    public void deleteProduct(Integer id) {
        if (!sanPhamRepository.existsById(id)) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }
        sanPhamRepository.deleteById(id);
    }
    
    // Lấy sản phẩm theo trạng thái
    public List<SanPham> getProductsByStatus(String trangThai) {
        return sanPhamRepository.findByTrangThai(trangThai);
    }
    
    // Lấy sản phẩm sắp hết hàng
    public List<SanPham> getLowStockProducts(Integer threshold) {
        if (threshold == null) threshold = 10; // Ngưỡng mặc định
        return sanPhamRepository.findLowStockProducts(threshold);
    }
    
    // Đếm số lượng sản phẩm theo trạng thái
    public long countProductsByStatus(String trangThai) {
        return sanPhamRepository.countByTrangThai(trangThai);
    }
    
    // Cập nhật số lượng sản phẩm
    public SanPham updateProductQuantity(Integer id, Integer soLuong) {
        SanPham product = sanPhamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        
        product.setSoLuong(soLuong);
        
        // Tự động cập nhật trạng thái
        if (soLuong <= 0) {
            product.setTrangThai("HETHANG");
        } else if ("HETHANG".equals(product.getTrangThai())) {
            product.setTrangThai("CONHANG");
        }
        
        return sanPhamRepository.save(product);
    }
}
