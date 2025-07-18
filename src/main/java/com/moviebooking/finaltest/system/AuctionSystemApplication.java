package com.moviebooking.finaltest.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSystemApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           🎯 HỆ THỐNG QUẢN LÝ SẢN PHẨM ĐẤU GIÁ             ║");
        System.out.println("║                 AUCTION PRODUCT MANAGEMENT                   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  ✅ Application Started Successfully!                        ║");
        System.out.println("║  🌐 URL: http://localhost:8080                              ║");
        System.out.println("║  ❤️  Health: http://localhost:8080/health                   ║");
        System.out.println("║  📋 Management: http://localhost:8080/san-pham/quan-ly      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}