package com.moviebooking.finaltest.system.repository;

import com.moviebooking.finaltest.system.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Long> {

    // Tìm theo tên loại sản phẩm
    List<LoaiSanPham> findByNameContainingIgnoreCase(String name);

    // Sắp xếp theo tên
    List<LoaiSanPham> findAllByOrderByNameAsc();

    // Kiểm tra tồn tại theo tên
    boolean existsByName(String name);
}