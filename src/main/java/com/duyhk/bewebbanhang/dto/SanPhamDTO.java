package com.duyhk.bewebbanhang.dto;

import com.duyhk.bewebbanhang.entity.LoaiSanPham;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamDTO {
    Long id;
    String ma;
    String ten;
    Long gia;
    Long soLuongTonKho;
    Long soLuongDaBan;
    String moTa;
    Integer trangThai;
    LoaiSanPhamDTO loaiSanPham;
    List<String> images;

    @JsonIgnore
    List<MultipartFile> files;
}
