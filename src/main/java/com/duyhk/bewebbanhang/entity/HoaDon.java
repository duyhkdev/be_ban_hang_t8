package com.duyhk.bewebbanhang.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    String maHoaDon;
    Long tongSoSanPham;
    Long tongSoTien;
    String soDienThoai;
    String diaChi;
    String hoVaTen;
    String maNhanVien;
    LocalDate ngayTao;
    LocalDate ngayHoanThanh;
    String lyDoHuy;
    Integer trangThai; // 1 đang cho, 2 cho lay hang, 3 dang giao hang, 4 da hoan thanh, 0 la huy
    Integer loaiHoaDon; // 1 online, 2 tại quầy
    @ManyToOne
    TaiKhoan taiKhoan;
}
