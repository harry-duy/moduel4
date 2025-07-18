package com.moviebooking.finaltest.system.config;

import com.moviebooking.finaltest.system.entity.LoaiSanPham;
import com.moviebooking.finaltest.system.entity.SanPham;
import com.moviebooking.finaltest.system.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // Chạy sau DataLoader chính
public class SanPhamDataLoader implements CommandLineRunner {

    @Autowired
    private SanPhamService sanPhamService;

    @Override
    public void run(String... args) throws Exception {
        // Tạo dữ liệu mẫu cho loại sản phẩm
        if (sanPhamService.getAllLoaiSanPham().isEmpty()) {
            createSampleLoaiSanPham();
        }

        // Tạo dữ liệu mẫu cho sản phẩm
        if (sanPhamService.countAllSanPham() == 0) {
            createSampleSanPham();
        }
    }

    private void createSampleLoaiSanPham() {
        // Tạo các loại sản phẩm mẫu
        LoaiSanPham dienThoai = new LoaiSanPham("Điện thoại");
        LoaiSanPham laptop = new LoaiSanPham("Laptop");
        LoaiSanPham tuLanh = new LoaiSanPham("Tủ lạnh");
        LoaiSanPham tivi = new LoaiSanPham("Tivi");
        LoaiSanPham xe = new LoaiSanPham("Xe");

        sanPhamService.saveLoaiSanPham(dienThoai);
        sanPhamService.saveLoaiSanPham(laptop);
        sanPhamService.saveLoaiSanPham(tuLanh);
        sanPhamService.saveLoaiSanPham(tivi);
        sanPhamService.saveLoaiSanPham(xe);
    }

    private void createSampleSanPham() {
        // Lấy danh sách loại sản phẩm đã tạo
        var loaiSanPhams = sanPhamService.getAllLoaiSanPham();

        if (loaiSanPhams.isEmpty()) {
            return;
        }

        LoaiSanPham dienThoai = loaiSanPhams.stream()
                .filter(l -> l.getName().equals("Điện thoại"))
                .findFirst().orElse(loaiSanPhams.get(0));

        LoaiSanPham laptop = loaiSanPhams.stream()
                .filter(l -> l.getName().equals("Laptop"))
                .findFirst().orElse(loaiSanPhams.get(0));

        LoaiSanPham tuLanh = loaiSanPhams.stream()
                .filter(l -> l.getName().equals("Tủ lạnh"))
                .findFirst().orElse(loaiSanPhams.get(0));

        LoaiSanPham tivi = loaiSanPhams.stream()
                .filter(l -> l.getName().equals("Tivi"))
                .findFirst().orElse(loaiSanPhams.get(0));

        LoaiSanPham xe = loaiSanPhams.stream()
                .filter(l -> l.getName().equals("Xe"))
                .findFirst().orElse(loaiSanPhams.get(0));

        // Tạo sản phẩm mẫu cho Điện thoại
        SanPham iphone11 = new SanPham("iPhone 11", 12000000.0, "chờ duyệt", dienThoai);
        SanPham iphone11Plus = new SanPham("iPhone 11 plus", 15000000.0, "chờ duyệt", dienThoai);
        SanPham iphone12 = new SanPham("iPhone 12", 18000000.0, "đang đấu giá", dienThoai);
        SanPham iphoneX = new SanPham("iPhone X", 12000000.0, "chờ duyệt", dienThoai);
        SanPham samsung = new SanPham("Samsung Galaxy S21", 16000000.0, "đang đấu giá", dienThoai);

        // Tạo sản phẩm mẫu cho Laptop
        SanPham macbook = new SanPham("MacBook Pro 13 inch", 25000000.0, "chờ duyệt", laptop);
        SanPham dell = new SanPham("Dell XPS 13", 20000000.0, "đang đấu giá", laptop);
        SanPham hp = new SanPham("HP Pavilion Gaming", 18000000.0, "chờ duyệt", laptop);

        // Tạo sản phẩm mẫu cho Tủ lạnh
        SanPham tuLanhToshiba = new SanPham("Tủ lạnh Toshiba 500L", 22000000.0, "đang đấu giá", tuLanh);
        SanPham tuLanhLG = new SanPham("Tủ lạnh LG Inverter 420L", 15000000.0, "chờ duyệt", tuLanh);

        // Tạo sản phẩm mẫu cho Tivi
        SanPham smartTivi = new SanPham("Smart Tivi Sony 55 inch", 12000000.0, "đã bán", tivi);
        SanPham tiviSamsung = new SanPham("Samsung QLED 65 inch", 25000000.0, "đang đấu giá", tivi);

        // Tạo sản phẩm mẫu cho Xe
        SanPham xeHonda = new SanPham("Honda Civic 2020", 650000000.0, "đang đấu giá", xe);
        SanPham xeToyota = new SanPham("Toyota Camry 2019", 850000000.0, "chờ duyệt", xe);

        // Lưu tất cả sản phẩm
        sanPhamService.saveSanPham(iphone11);
        sanPhamService.saveSanPham(iphone11Plus);
        sanPhamService.saveSanPham(iphone12);
        sanPhamService.saveSanPham(iphoneX);
        sanPhamService.saveSanPham(samsung);
        sanPhamService.saveSanPham(macbook);
        sanPhamService.saveSanPham(dell);
        sanPhamService.saveSanPham(hp);
        sanPhamService.saveSanPham(tuLanhToshiba);
        sanPhamService.saveSanPham(tuLanhLG);
        sanPhamService.saveSanPham(smartTivi);
        sanPhamService.saveSanPham(tiviSamsung);
        sanPhamService.saveSanPham(xeHonda);
        sanPhamService.saveSanPham(xeToyota);

        System.out.println("Đã tạo dữ liệu mẫu cho " + sanPhamService.countAllSanPham() + " sản phẩm!");
    }
}