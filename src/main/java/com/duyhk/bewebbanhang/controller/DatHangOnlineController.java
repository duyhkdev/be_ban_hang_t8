package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.dto.ThongTinDatHangDTO;
import com.duyhk.bewebbanhang.dto.ThongTinHoaDonDTO;
import com.duyhk.bewebbanhang.service.DatHangOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online")
@RequiredArgsConstructor
public class DatHangOnlineController {
    private final DatHangOnlineService datHangOnlineService;

    @GetMapping("/{id}")
    public ResponseDTO<ThongTinHoaDonDTO> getThongTin(@PathVariable Long id) {
        return ResponseDTO.<ThongTinHoaDonDTO>builder()
                .status(200)
                .data(datHangOnlineService.getThongTinHoaDon(id))
                .build();
    }

    @PostMapping
    public ResponseDTO<Void> datHang(@RequestBody ThongTinDatHangDTO thongTinDatHangDTO) {
        return ResponseDTO.<Void>builder()
                .message(datHangOnlineService.datHang(thongTinDatHangDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseDTO<Void> updateTrangThai(@PathVariable Long id, @RequestParam Integer trangThai) {
        return ResponseDTO.<Void>builder()
                .message(datHangOnlineService.updateTrangThai(id, trangThai))
                .build();
    }
}
