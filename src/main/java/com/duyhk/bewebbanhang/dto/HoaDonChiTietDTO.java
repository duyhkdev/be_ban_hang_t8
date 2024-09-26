package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDonChiTietDTO {
    Long id;
    Long donGia;
    Long soLuong;
    Long thanhTien;
    SanPhamChiTietDTO sanPhamChiTiet;
}
