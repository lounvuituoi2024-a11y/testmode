package com.example.testmode.service;

import com.example.testmode.dto.ChangePasswordRequest;
import com.example.testmode.dto.LoginRequest;
import com.example.testmode.dto.LoginResponse;
import com.example.testmode.dto.UpdateUserRequest;
import com.example.testmode.model.NguoiDung;
import com.example.testmode.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Đăng nhập
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(loginRequest.getTenDangNhap());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Tên đăng nhập không tồn tại");
        }
        
        NguoiDung user = userOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getMatKhau(), user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
        
        if ("KHOA".equals(user.getTrangThai())) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }
        
        return new LoginResponse(
            user.getId(),
            user.getTenDangNhap(),
            user.getHoTen(),
            user.getEmail(),
            user.getSoDienThoai(),
            user.getDiaChi(),
            user.getQuyen(),
            user.getTrangThai()
        );
    }
    
    // Lấy tất cả người dùng với phân trang
    public Page<NguoiDung> getAllUsers(Pageable pageable) {
        return nguoiDungRepository.findAll(pageable);
    }
    
    // Tìm kiếm người dùng
    public Page<NguoiDung> searchUsers(String keyword, String quyen, String trangThai, Pageable pageable) {
        if (keyword == null) keyword = "";
        
        if (quyen != null && trangThai != null) {
            // Cần thêm method trong repository nếu muốn filter cả 2
            return nguoiDungRepository.findByKeywordAndQuyen(keyword, quyen, pageable);
        } else if (quyen != null) {
            return nguoiDungRepository.findByKeywordAndQuyen(keyword, quyen, pageable);
        } else if (trangThai != null) {
            return nguoiDungRepository.findByKeywordAndTrangThai(keyword, trangThai, pageable);
        } else {
            return nguoiDungRepository.findByKeyword(keyword, pageable);
        }
    }
    
    // Tìm người dùng theo ID
    public Optional<NguoiDung> findById(Integer id) {
        return nguoiDungRepository.findById(id);
    }
    
    // Tìm người dùng theo username
    public Optional<NguoiDung> findByUsername(String username) {
        return nguoiDungRepository.findByTenDangNhap(username);
    }
    
    // Tạo người dùng mới
    public NguoiDung createUser(NguoiDung nguoiDung) {
        // Kiểm tra trùng tên đăng nhập
        if (nguoiDungRepository.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        // Kiểm tra trùng email
        if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        // Mã hóa mật khẩu
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        
        return nguoiDungRepository.save(nguoiDung);
    }
    
    // Cập nhật thông tin người dùng
    public NguoiDung updateUser(Integer id, UpdateUserRequest updateRequest) {
        NguoiDung user = nguoiDungRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        // Kiểm tra email trùng (trừ chính user hiện tại)
        if (nguoiDungRepository.existsByEmailAndIdNot(updateRequest.getEmail(), id)) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        user.setHoTen(updateRequest.getHoTen());
        user.setEmail(updateRequest.getEmail());
        user.setSoDienThoai(updateRequest.getSoDienThoai());
        user.setDiaChi(updateRequest.getDiaChi());
        
        return nguoiDungRepository.save(user);
    }
    
    // Cập nhật người dùng (nhận NguoiDung object)
    public NguoiDung updateUser(Integer id, NguoiDung nguoiDung) {
        NguoiDung existingUser = nguoiDungRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        existingUser.setHoTen(nguoiDung.getHoTen());
        existingUser.setEmail(nguoiDung.getEmail());
        existingUser.setSoDienThoai(nguoiDung.getSoDienThoai());
        existingUser.setDiaChi(nguoiDung.getDiaChi());
        existingUser.setQuyen(nguoiDung.getQuyen());
        existingUser.setTrangThai(nguoiDung.getTrangThai());
        
        // Chỉ cập nhật mật khẩu nếu có giá trị mới và khác với mật khẩu hiện tại
        if (nguoiDung.getMatKhau() != null && !nguoiDung.getMatKhau().trim().isEmpty()) {
            // Nếu mật khẩu mới khác với mật khẩu hiện tại (đã mã hóa), thì mã hóa mật khẩu mới
            if (!nguoiDung.getMatKhau().equals(existingUser.getMatKhau())) {
                existingUser.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
            }
        }
        
        return nguoiDungRepository.save(existingUser);
    }
    
    // Thay đổi mật khẩu
    public void changePassword(Integer userId, ChangePasswordRequest changePasswordRequest) {
        NguoiDung user = nguoiDungRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(changePasswordRequest.getMatKhauHienTai(), user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }
        
        // Kiểm tra mật khẩu mới và xác nhận
        if (!changePasswordRequest.getMatKhauMoi().equals(changePasswordRequest.getXacNhanMatKhau())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận không khớp");
        }
        
        // Cập nhật mật khẩu mới
        user.setMatKhau(passwordEncoder.encode(changePasswordRequest.getMatKhauMoi()));
        nguoiDungRepository.save(user);
    }
    
    // Cập nhật quyền người dùng (Admin only)
    public NguoiDung updateUserRole(Integer id, String quyen) {
        NguoiDung user = nguoiDungRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        user.setQuyen(quyen);
        return nguoiDungRepository.save(user);
    }
    
    // Cập nhật trạng thái người dùng (Admin only)
    public NguoiDung updateUserStatus(Integer id, String trangThai) {
        NguoiDung user = nguoiDungRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        user.setTrangThai(trangThai);
        return nguoiDungRepository.save(user);
    }
    
    // Xóa người dùng (Admin only)
    public void deleteUser(Integer id) {
        if (!nguoiDungRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        nguoiDungRepository.deleteById(id);
    }
    
    // Lấy người dùng theo quyền
    public List<NguoiDung> getUsersByRole(String quyen) {
        return nguoiDungRepository.findByQuyen(quyen);
    }
    
    // Thống kê cho dashboard
    public long getTotalUsers() {
        return nguoiDungRepository.count();
    }
    
    public long countByTrangThai(String trangThai) {
        return nguoiDungRepository.countByTrangThai(trangThai);
    }
    
    public long countByQuyen(String quyen) {
        return nguoiDungRepository.countByQuyen(quyen);
    }
    
    public NguoiDung findByTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap)
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
    }
}
