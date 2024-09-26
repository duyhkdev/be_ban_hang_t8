package com.duyhk.bewebbanhang.repository;

import com.duyhk.bewebbanhang.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GioHangChiTietRepo extends JpaRepository<GioHangChiTiet, Long> {
    /** Select * from gio_hang_chi_tiet where gio_hang_id = ?*/
    Optional<List<GioHangChiTiet>> findByGioHangId(Long id);

    Optional<GioHangChiTiet> findBySanPhamChiTietIdAndGioHangId(Long id, Long family);


    @Query(value = "select g from GioHangChiTiet g")
    Optional<List<GioHangChiTiet>> getAll();

    @Query(value = "select * from gio_hang_chi_tiet", nativeQuery = true)
    Optional<List<GioHangChiTiet>> getAll2();
}
