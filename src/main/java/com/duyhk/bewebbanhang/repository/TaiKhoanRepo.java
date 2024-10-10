package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaiKhoanRepo extends JpaRepository<TaiKhoan, Long> {
    Optional<TaiKhoan> findBySoDienThoai(String soDienThoai);
}
