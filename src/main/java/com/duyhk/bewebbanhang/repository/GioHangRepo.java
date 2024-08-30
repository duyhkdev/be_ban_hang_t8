package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface GioHangRepo extends JpaRepository<GioHang, Long> {
}
