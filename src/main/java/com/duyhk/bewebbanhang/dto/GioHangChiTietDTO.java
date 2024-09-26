package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GioHangChiTietDTO {
    Long id;
    Long soLuong;
    SanPhamChiTietDTO sanPhamChiTiet;
    Long gioHangId;
}
