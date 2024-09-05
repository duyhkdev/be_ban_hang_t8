package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamChiTietDTO {
    Long id;
    String ma;
    String ten;
    Long gia;
    Long soLuongTonKho;
    Long soLuongDaBan;
    Integer trangThai;
    //
    MauSacDTO mauSac;
    KichCoDTO kichCo;
    SanPhamDTO sanPham;
}
