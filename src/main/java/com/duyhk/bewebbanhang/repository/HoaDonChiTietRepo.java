package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepo extends JpaRepository<HoaDonChiTiet, Long> {
    Optional<List<HoaDonChiTiet>> findByHoaDonId(Long id);
}
