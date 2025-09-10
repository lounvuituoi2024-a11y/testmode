package com.example.testmode.dto;

public class UserStatsResponse {
    private long admin;
    private long nhanvien;
    private long khachhang;
    private long total;

    public UserStatsResponse() {}

    public UserStatsResponse(long admin, long nhanvien, long khachhang) {
        this.admin = admin;
        this.nhanvien = nhanvien;
        this.khachhang = khachhang;
        this.total = admin + nhanvien + khachhang;
    }

    public long getAdmin() {
        return admin;
    }

    public void setAdmin(long admin) {
        this.admin = admin;
    }

    public long getNhanvien() {
        return nhanvien;
    }

    public void setNhanvien(long nhanvien) {
        this.nhanvien = nhanvien;
    }

    public long getKhachhang() {
        return khachhang;
    }

    public void setKhachhang(long khachhang) {
        this.khachhang = khachhang;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
