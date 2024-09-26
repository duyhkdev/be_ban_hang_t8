package com.duyhk.bewebbanhang.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    Long donGia;
    Long soLuong;
    Long thanhTien;
    @ManyToOne
    @JoinColumn(name = "san_pham_chi_tiet_id")
    SanPhamChiTiet sanPhamChiTiet;
    @ManyToOne
    @JoinColumn(name = "hoa_don_id")
    HoaDon hoaDon;

    public void setSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        this.sanPhamChiTiet = sanPhamChiTiet;
        this.donGia = sanPhamChiTiet.getGia();
    }
}
