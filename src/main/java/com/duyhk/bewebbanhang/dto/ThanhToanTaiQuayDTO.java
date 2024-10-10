package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ThanhToanTaiQuayDTO {
    Long hoaDonId;
    String soDienThoai;
    Long soTienKhachDua;
}
