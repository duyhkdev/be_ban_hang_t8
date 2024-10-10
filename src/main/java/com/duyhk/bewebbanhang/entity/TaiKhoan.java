package com.duyhk.bewebbanhang.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tai_khoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String ma;
    String soDienThoai;
    String email;
    String matKhau;
    String hoVaTen;
    Role role;
    Long tongHoaDon;
    Long tongTien;
    Integer hangTaiKhoan; // 1 là thường, 2 là vip
    Integer trangThai; // 0 LÀ NGỪNG HOẠT ĐỘNG, 1 LÀ HOẠT ĐỘNG
}
