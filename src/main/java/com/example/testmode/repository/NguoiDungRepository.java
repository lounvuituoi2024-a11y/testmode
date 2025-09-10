package com.example.testmode.repository;

import com.example.testmode.model.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    
    // Tìm người dùng theo tên đăng nhập
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);
    
    // Kiểm tra tồn tại theo tên đăng nhập
    boolean existsByTenDangNhap(String tenDangNhap);
    
    // Kiểm tra tồn tại theo email
    boolean existsByEmail(String email);
    
    // Tìm người dùng theo email
    Optional<NguoiDung> findByEmail(String email);
    
    // Tìm người dùng theo quyền
    List<NguoiDung> findByQuyen(String quyen);
    
    // Tìm người dùng theo trạng thái
    List<NguoiDung> findByTrangThai(String trangThai);
    
    // Tìm kiếm người dùng theo từ khóa (tên đăng nhập, họ tên, email)
    @Query("SELECT n FROM NguoiDung n WHERE " +
           "n.tenDangNhap LIKE %:keyword% OR " +
           "n.hoTen LIKE %:keyword% OR " +
           "n.email LIKE %:keyword%")
    Page<NguoiDung> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Tìm kiếm và lọc theo quyền
    @Query("SELECT n FROM NguoiDung n WHERE " +
           "(n.tenDangNhap LIKE %:keyword% OR " +
           "n.hoTen LIKE %:keyword% OR " +
           "n.email LIKE %:keyword%) AND " +
           "n.quyen = :quyen")
    Page<NguoiDung> findByKeywordAndQuyen(@Param("keyword") String keyword, 
                                          @Param("quyen") String quyen, 
                                          Pageable pageable);
    
    // Tìm kiếm và lọc theo trạng thái
    @Query("SELECT n FROM NguoiDung n WHERE " +
           "(n.tenDangNhap LIKE %:keyword% OR " +
           "n.hoTen LIKE %:keyword% OR " +
           "n.email LIKE %:keyword%) AND " +
           "n.trangThai = :trangThai")
    Page<NguoiDung> findByKeywordAndTrangThai(@Param("keyword") String keyword, 
                                              @Param("trangThai") String trangThai, 
                                              Pageable pageable);
    
    // Kiểm tra email trùng khi cập nhật (loại trừ ID hiện tại)
    boolean existsByEmailAndIdNot(String email, Integer id);
    
    // Đếm theo trạng thái
    long countByTrangThai(String trangThai);
    
    // Đếm theo quyền
    long countByQuyen(String quyen);
}