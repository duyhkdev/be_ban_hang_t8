package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HoaDonRepo extends JpaRepository<HoaDon, Long> {
    Optional<HoaDon> findByMaHoaDon(String maDonHang);
}
