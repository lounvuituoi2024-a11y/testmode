package com.example.testmode.service;

import com.example.testmode.model.NguoiDung;
import com.example.testmode.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);
        
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Không tìm thấy user: " + username);
        }
        
        NguoiDung user = userOpt.get();
        
        // Kiểm tra trạng thái
        if ("KHOA".equals(user.getTrangThai())) {
            throw new UsernameNotFoundException("Tài khoản đã bị khóa: " + username);
        }
        
        // Tạo authority với prefix ROLE_
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getQuyen());
        
        return User.builder()
                .username(user.getTenDangNhap())
                .password(user.getMatKhau())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked("KHOA".equals(user.getTrangThai()))
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
