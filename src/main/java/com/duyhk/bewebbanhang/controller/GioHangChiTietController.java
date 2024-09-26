package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.GioHangChiTietDTO;
import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.service.GioHangChiTietService;
import com.duyhk.bewebbanhang.service.iplm.GioHangChiTietServiceIplm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gio-hang-chi-tiet")
public class GioHangChiTietController {
    private final GioHangChiTietService gioHangChiTietService;
    @GetMapping
    public ResponseDTO<List<GioHangChiTietDTO>> getByGioHangId(@RequestParam Long id){
        return ResponseDTO.<List<GioHangChiTietDTO>>builder()
                .status(201)
                .data(gioHangChiTietService.getByGoHangId(id))
                .build();
    }

    @PostMapping
    public ResponseDTO<Void> create(@RequestBody GioHangChiTietDTO dto){
        return ResponseDTO.<Void>builder()
                .status(200)
               .message(gioHangChiTietService.themVaoGioHang(dto))
               .build();
    }
 // localhost:8080/api/gio-hang-chi-tiet/1?soLuong=2
    @PutMapping("/{id}")
    public ResponseDTO<Void> update(@PathVariable Long id, @RequestParam Long soLuong){
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(gioHangChiTietService.suaSoLuong(soLuong, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable Long id){
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(gioHangChiTietService.xoaKhoiGioHang(id))
                .build();
    }

}
