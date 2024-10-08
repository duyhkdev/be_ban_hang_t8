package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ThemVaoHoaDonRequest {
    Long idHoaDon;
    Long idSpct;
    Long soLuong;
}
