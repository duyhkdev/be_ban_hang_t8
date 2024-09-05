package com.duyhk.bewebbanhang.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KichCoDTO {
    Long id;
    String ten;
}
