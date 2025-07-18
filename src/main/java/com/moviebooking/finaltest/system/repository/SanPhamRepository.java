package com.moviebooking.finaltest.system.repository;


import com.moviebooking.finaltest.system.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    // Tìm kiếm theo tên sản phẩm
    Page<SanPham> findByNameContainingIgnoreCaseOrderById(String name, Pageable pageable);

    // Tìm kiếm theo loại sản phẩm
    Page<SanPham> findByLoaiSanPham_CidOrderById(Long loaiSanPhamId, Pageable pageable);

    // Tìm kiếm theo giá bắt đầu
    Page<SanPham> findByPriceGreaterThanEqualOrderById(Double price, Pageable pageable);

    // Tìm kiếm tổng hợp
    @Query("SELECT s FROM SanPham s WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:loaiSanPhamId IS NULL OR s.loaiSanPham.cid = :loaiSanPhamId) AND " +
            "(:price IS NULL OR s.price >= :price) " +
            "ORDER BY s.id")
    Page<SanPham> findSanPhamWithFilters(@Param("name") String name,
                                         @Param("loaiSanPhamId") Long loaiSanPhamId,
                                         @Param("price") Double price,
                                         Pageable pageable);

    // Tất cả sản phẩm có phân trang
    Page<SanPham> findAllByOrderById(Pageable pageable);

    // Đếm số lượng sản phẩm theo loại
    long countByLoaiSanPham_Cid(Long loaiSanPhamId);
}