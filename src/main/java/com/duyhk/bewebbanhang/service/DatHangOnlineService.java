package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.ThongTinDatHangRequest;
import com.duyhk.bewebbanhang.dto.ThongTinDatHangKhongDangNhapDTO;
import com.duyhk.bewebbanhang.dto.ThongTinHoaDonDTO;

public interface DatHangOnlineService {
    ThongTinHoaDonDTO getThongTinHoaDon(Long id);
    String datHang(ThongTinDatHangRequest dto);
    String datHangKhongDangNhap(ThongTinDatHangKhongDangNhapDTO dto);
    String updateTrangThai(Long id, Integer trangThai);
}
