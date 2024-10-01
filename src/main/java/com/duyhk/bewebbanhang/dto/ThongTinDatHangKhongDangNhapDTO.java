package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThongTinDatHangKhongDangNhapDTO {
    String soDienThoai;
    String diaChi;
    String hoVaTen;
    List<GioHangChiTietDTO> listGioHang;
}
