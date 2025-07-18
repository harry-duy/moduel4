package com.moviebooking.finaltest.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 5, max = 50, message = "Tên sản phẩm phải từ 5 đến 50 ký tự")
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull(message = "Giá bắt đầu không được để trống")
    @DecimalMin(value = "100000", message = "Giá bắt đầu phải tối thiểu 100,000 VND")
    private Double price;

    @Column(name = "status", nullable = false)
    @NotBlank(message = "Tình trạng không được để trống")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loai_sp", nullable = false)
    @NotNull(message = "Loại sản phẩm không được để trống")
    private LoaiSanPham loaiSanPham;

    // Constructors
    public SanPham() {}

    public SanPham(String name, Double price, String status, LoaiSanPham loaiSanPham) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.loaiSanPham = loaiSanPham;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    // Helper method to format price
    public String getFormattedPrice() {
        return String.format("%,.0f VND", price);
    }
}