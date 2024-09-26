package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.ThongTinDatHangDTO;
import com.duyhk.bewebbanhang.dto.ThongTinHoaDonDTO;

public interface DatHangOnlineService {
    ThongTinHoaDonDTO getThongTinHoaDon(Long id);
    String datHang(ThongTinDatHangDTO dto);
    String updateTrangThai(Long id, Integer trangThai);
}
