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

    // Tìm kiếm theo tên sản phẩm với JOIN FETCH
    @Query("SELECT s FROM SanPham s JOIN FETCH s.loaiSanPham WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY s.id")
    Page<SanPham> findByNameContainingIgnoreCaseOrderById(@Param("name") String name, Pageable pageable);

    // Tìm kiếm theo loại sản phẩm với JOIN FETCH
    @Query("SELECT s FROM SanPham s JOIN FETCH s.loaiSanPham WHERE s.loaiSanPham.cid = :loaiSanPhamId ORDER BY s.id")
    Page<SanPham> findByLoaiSanPham_CidOrderById(@Param("loaiSanPhamId") Long loaiSanPhamId, Pageable pageable);

    // Tìm kiếm theo giá bắt đầu với JOIN FETCH
    @Query("SELECT s FROM SanPham s JOIN FETCH s.loaiSanPham WHERE s.price >= :price ORDER BY s.id")
    Page<SanPham> findByPriceGreaterThanEqualOrderById(@Param("price") Double price, Pageable pageable);

    // Tìm kiếm tổng hợp với JOIN FETCH
    @Query("SELECT s FROM SanPham s JOIN FETCH s.loaiSanPham WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:loaiSanPhamId IS NULL OR s.loaiSanPham.cid = :loaiSanPhamId) AND " +
            "(:price IS NULL OR s.price >= :price) " +
            "ORDER BY s.id")
    Page<SanPham> findSanPhamWithFilters(@Param("name") String name,
                                         @Param("loaiSanPhamId") Long loaiSanPhamId,
                                         @Param("price") Double price,
                                         Pageable pageable);

    // Tất cả sản phẩm có phân trang với JOIN FETCH
    @Query("SELECT s FROM SanPham s JOIN FETCH s.loaiSanPham ORDER BY s.id")
    Page<SanPham> findAllByOrderById(Pageable pageable);

    // Đếm số lượng sản phẩm theo loại
    long countByLoaiSanPham_Cid(Long loaiSanPhamId);
}