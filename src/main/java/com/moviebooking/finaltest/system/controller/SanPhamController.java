package com.moviebooking.finaltest.system.controller;


import com.moviebooking.finaltest.system.entity.LoaiSanPham;
import com.moviebooking.finaltest.system.entity.SanPham;
import com.moviebooking.finaltest.system.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    // Trang quản lý sản phẩm - Yêu cầu 1
    @GetMapping("/quan-ly")
    public String quanLySanPham(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) Long loaiSanPham,
                                @RequestParam(required = false) Double price,
                                Model model) {

        Page<SanPham> sanPhamPage;

        // Kiểm tra có filter không
        if ((name != null && !name.trim().isEmpty()) ||
                loaiSanPham != null ||
                price != null) {
            // Tìm kiếm với filter
            sanPhamPage = sanPhamService.searchSanPham(name, loaiSanPham, price, page);
        } else {
            // Lấy tất cả
            sanPhamPage = sanPhamService.getAllSanPham(page);
        }

        // Lấy danh sách loại sản phẩm cho dropdown
        List<LoaiSanPham> loaiSanPhams = sanPhamService.getAllLoaiSanPham();

        model.addAttribute("sanPhams", sanPhamPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("totalElements", sanPhamPage.getTotalElements());
        model.addAttribute("hasNext", sanPhamPage.hasNext());
        model.addAttribute("hasPrevious", sanPhamPage.hasPrevious());
        model.addAttribute("loaiSanPhams", loaiSanPhams);

        // Giữ lại filter parameters
        model.addAttribute("name", name);
        model.addAttribute("loaiSanPham", loaiSanPham);
        model.addAttribute("price", price);

        return "san-pham/quan-ly";
    }

    // Xóa sản phẩm - Yêu cầu 2
    @PostMapping("/xoa")
    public String xoaSanPham(@RequestParam("selectedIds") List<Long> selectedIds,
                             RedirectAttributes redirectAttributes) {
        try {
            if (selectedIds != null && !selectedIds.isEmpty()) {
                sanPhamService.deleteSanPhamByIds(selectedIds);
                redirectAttributes.addFlashAttribute("success",
                        "Đã xóa thành công " + selectedIds.size() + " sản phẩm!");
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "Vui lòng chọn ít nhất một sản phẩm để xóa!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi xóa sản phẩm: " + e.getMessage());
        }

        return "redirect:/san-pham/quan-ly";
    }

    // Trang thêm sản phẩm - Yêu cầu 3
    @GetMapping("/them")
    public String themSanPhamForm(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("loaiSanPhams", sanPhamService.getAllLoaiSanPham());
        return "san-pham/them";
    }

    // Xử lý thêm sản phẩm - Yêu cầu 3
    @PostMapping("/them")
    public String themSanPham(@Valid @ModelAttribute SanPham sanPham,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("loaiSanPhams", sanPhamService.getAllLoaiSanPham());
            return "san-pham/them";
        }

        try {
            sanPhamService.saveSanPham(sanPham);
            redirectAttributes.addFlashAttribute("success",
                    "Thêm sản phẩm thành công!");
            return "redirect:/san-pham/quan-ly";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi thêm sản phẩm: " + e.getMessage());
            return "redirect:/san-pham/them";
        }
    }

    // Trang sửa sản phẩm
    @GetMapping("/sua/{id}")
    public String suaSanPhamForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<SanPham> sanPhamOpt = sanPhamService.getSanPhamById(id);

        if (sanPhamOpt.isPresent()) {
            model.addAttribute("sanPham", sanPhamOpt.get());
            model.addAttribute("loaiSanPhams", sanPhamService.getAllLoaiSanPham());
            return "san-pham/sua";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/san-pham/quan-ly";
        }
    }

    // Xử lý sửa sản phẩm
    @PostMapping("/sua/{id}")
    public String suaSanPham(@PathVariable Long id,
                             @Valid @ModelAttribute SanPham sanPham,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("loaiSanPhams", sanPhamService.getAllLoaiSanPham());
            return "san-pham/sua";
        }

        try {
            sanPham.setId(id);
            sanPhamService.updateSanPham(sanPham);
            redirectAttributes.addFlashAttribute("success",
                    "Cập nhật sản phẩm thành công!");
            return "redirect:/san-pham/quan-ly";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi cập nhật sản phẩm: " + e.getMessage());
            return "redirect:/san-pham/sua/" + id;
        }
    }

    // API để xóa thông tin tìm kiếm
    @PostMapping("/clear-search")
    public String clearSearch() {
        return "redirect:/san-pham/quan-ly";
    }
}
