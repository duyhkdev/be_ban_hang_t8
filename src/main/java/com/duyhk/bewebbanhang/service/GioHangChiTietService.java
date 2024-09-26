package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.GioHangChiTietDTO;
import com.duyhk.bewebbanhang.entity.GioHangChiTiet;

import java.util.List;

public interface GioHangChiTietService {
    List<GioHangChiTietDTO> getByGoHangId(Long id);
    String themVaoGioHang(GioHangChiTietDTO gioHangChiTiet);
    String suaSoLuong(Long soLuong, Long id);
    String xoaKhoiGioHang(Long id);
}
