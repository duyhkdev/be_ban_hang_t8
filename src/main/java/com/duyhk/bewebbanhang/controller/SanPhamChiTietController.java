package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.dto.SanPhamChiTietDTO;
import com.duyhk.bewebbanhang.service.SanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/san-pham-chi-tiet")
@RequiredArgsConstructor
public class SanPhamChiTietController {

    private final SanPhamChiTietService sanPhamChiTietService;

    @GetMapping
    public ResponseDTO<List<SanPhamChiTietDTO>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        return sanPhamChiTietService.getAll(page, pageSize);
    }

    @PostMapping
    public ResponseDTO<Void> create(@RequestBody SanPhamChiTietDTO dto) {
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(sanPhamChiTietService.create(dto))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseDTO<Void> update(@RequestBody SanPhamChiTietDTO dto, @PathVariable Long id) throws IOException {
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(sanPhamChiTietService.update(dto, id))
                .build();
    }

    @DeleteMapping
    public ResponseDTO<Void> delete(@PathVariable Long id) {
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(sanPhamChiTietService.delete(id))
                .build();
    }
}
