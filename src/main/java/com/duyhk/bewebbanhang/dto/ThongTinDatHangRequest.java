package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThongTinDatHangRequest {
    String soDienThoai;
    String diaChi;
    String hoVaTen;
    Long taiKhoanId;
}
