package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDonDTO {
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
    Integer trangThai;
    Integer loaiHoaDon;
}
