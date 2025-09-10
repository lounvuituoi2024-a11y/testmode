package com.example.testmode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten", nullable = false, length = 100)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String ten;

    @Column(name = "gia", nullable = false, precision = 18, scale = 2)
    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal gia;

    @Column(name = "so_luong", nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer soLuong;

    @Column(name = "mo_ta", length = 500)
    private String moTa;

    @Column(name = "trang_thai", length = 20)
    private String trangThai; // CONHANG, HETHANG, NGUNGBAN

    // Constructors
    public SanPham() {}

    public SanPham(String ten, BigDecimal gia, Integer soLuong, String moTa, String trangThai) {
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", gia=" + gia +
                ", soLuong=" + soLuong +
                ", moTa='" + moTa + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
