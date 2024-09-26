package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
public interface GioHangRepo extends JpaRepository<GioHang, Long> {
    Optional<GioHang> findByTaiKhoanId(Long taiKhoanId);
}
