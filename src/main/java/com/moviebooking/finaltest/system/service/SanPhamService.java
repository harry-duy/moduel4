package com.moviebooking.finaltest.system.service;


import com.moviebooking.finaltest.system.entity.LoaiSanPham;
import com.moviebooking.finaltest.system.entity.SanPham;
import com.moviebooking.finaltest.system.repository.LoaiSanPhamRepository;
import com.moviebooking.finaltest.system.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    // Lấy tất cả sản phẩm với phân trang (5 sản phẩm/trang)
    public Page<SanPham> getAllSanPham(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return sanPhamRepository.findAllByOrderById(pageable);
    }

    // Tìm kiếm sản phẩm với filters và phân trang
    public Page<SanPham> searchSanPham(String name, Long loaiSanPhamId, Double price, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return sanPhamRepository.findSanPhamWithFilters(name, loaiSanPhamId, price, pageable);
    }

    // Lấy sản phẩm theo ID
    public Optional<SanPham> getSanPhamById(Long id) {
        return sanPhamRepository.findById(id);
    }

    // Thêm sản phẩm mới
    public SanPham saveSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    // Cập nhật sản phẩm
    public SanPham updateSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    // Xóa sản phẩm theo ID
    public void deleteSanPhamById(Long id) {
        sanPhamRepository.deleteById(id);
    }

    // Xóa nhiều sản phẩm
    public void deleteSanPhamByIds(List<Long> ids) {
        sanPhamRepository.deleteAllById(ids);
    }

    // Lấy tất cả loại sản phẩm
    public List<LoaiSanPham> getAllLoaiSanPham() {
        return loaiSanPhamRepository.findAllByOrderByNameAsc();
    }

    // Lấy loại sản phẩm theo ID
    public Optional<LoaiSanPham> getLoaiSanPhamById(Long id) {
        return loaiSanPhamRepository.findById(id);
    }

    // Thêm loại sản phẩm mới
    public LoaiSanPham saveLoaiSanPham(LoaiSanPham loaiSanPham) {
        return loaiSanPhamRepository.save(loaiSanPham);
    }

    // Đếm tổng số sản phẩm
    public long countAllSanPham() {
        return sanPhamRepository.count();
    }

    // Đếm số sản phẩm theo loại
    public long countSanPhamByLoai(Long loaiSanPhamId) {
        return sanPhamRepository.countByLoaiSanPham_Cid(loaiSanPhamId);
    }
}
