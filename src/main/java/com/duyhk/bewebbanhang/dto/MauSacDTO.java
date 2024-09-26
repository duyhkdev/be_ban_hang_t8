package com.duyhk.bewebbanhang.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MauSacDTO {
    Long id;
    @NotNull(message = "Ten khong duoc de trong")
    String ten;
}
