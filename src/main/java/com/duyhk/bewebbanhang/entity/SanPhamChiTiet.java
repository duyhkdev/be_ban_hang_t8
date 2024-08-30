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
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String ma;
    String ten;
    Long gia;
    Long soLuongTonKho;
    Long soLuongDaBan;
    Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "mau_sac_id")
    MauSac mauSac;

    @ManyToOne
    @JoinColumn(name = "kich_co_id")
    KichCo kichCo;
    // đen

    @ManyToOne
    @JoinColumn(name = "san_pham_id")
    SanPham sanPham;
    /*
    so_luong_ton_kho bigint, so_luong_da_ban bigint, trang_thai int,
     san_pham_id bigint (khóa phụ), mau_sac_id bigint (khóa phụ), kich_co_id bigint (khóa phụ)

     */
}
