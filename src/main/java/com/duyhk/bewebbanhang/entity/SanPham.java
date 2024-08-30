package com.duyhk.bewebbanhang.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String ma;
    String ten;
    Long gia;
    Long soLuongTonKho;
    Long soLuongDaBan;
    String moTa;
    Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id")
    LoaiSanPham loaiSanPham;

    //anh_san_pham: product_id bigint, images varchar(255)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "anh_san_pham")
    List<String> images;
}
