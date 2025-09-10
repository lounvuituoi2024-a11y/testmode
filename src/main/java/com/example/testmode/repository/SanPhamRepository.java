package com.example.testmode.repository;

import com.example.testmode.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    
    // Tìm sản phẩm theo tên chứa từ khóa
    List<SanPham> findByTenContaining(String keyword);
    
    // Tìm sản phẩm theo trạng thái
    List<SanPham> findByTrangThai(String trangThai);
    
    // Tìm kiếm sản phẩm với phân trang
    @Query("SELECT s FROM SanPham s WHERE s.ten LIKE %:keyword%")
    Page<SanPham> findByTenContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // Tìm kiếm và lọc theo trạng thái với phân trang
    @Query("SELECT s FROM SanPham s WHERE s.ten LIKE %:keyword% AND s.trangThai = :trangThai")
    Page<SanPham> findByTenContainingAndTrangThai(@Param("keyword") String keyword, 
                                                  @Param("trangThai") String trangThai, 
                                                  Pageable pageable);
    
    // Tìm sản phẩm theo trạng thái với phân trang
    Page<SanPham> findByTrangThai(String trangThai, Pageable pageable);
    
    // Tìm kiếm tổng quát trong tên và mô tả
    @Query("SELECT s FROM SanPham s WHERE " +
           "s.ten LIKE %:keyword% OR " +
           "s.moTa LIKE %:keyword%")
    Page<SanPham> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Đếm số lượng sản phẩm theo trạng thái
    long countByTrangThai(String trangThai);
    
    // Tìm sản phẩm có số lượng thấp hơn ngưỡng
    @Query("SELECT s FROM SanPham s WHERE s.soLuong < :threshold")
    List<SanPham> findLowStockProducts(@Param("threshold") Integer threshold);
}