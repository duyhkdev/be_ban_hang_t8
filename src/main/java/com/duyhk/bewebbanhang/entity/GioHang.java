package com.duyhk.bewebbanhang.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long tongSoSanPham;
    Long tongSoTien;
    @OneToOne
    TaiKhoan taiKhoan;
}
