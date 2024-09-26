package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Long> {

    @Query("""
                select s from SanPhamChiTiet s 
                where s.soLuongTonKho >= :soLuongMua 
                and s.trangThai = 1 
                and s.id = :id
            """)
    Optional<SanPhamChiTiet> checkSoLuongDatHang(
            @Param("soLuongTonKho") Long soLuongMua,
            @Param("id") Long id);
}
