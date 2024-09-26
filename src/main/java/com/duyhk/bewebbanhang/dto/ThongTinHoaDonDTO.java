package com.duyhk.bewebbanhang.dto;

import com.duyhk.bewebbanhang.entity.HoaDonChiTiet;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThongTinHoaDonDTO {
    HoaDonDTO thongTinChung;
    List<HoaDonChiTietDTO> thongTinChiTiet;
}
