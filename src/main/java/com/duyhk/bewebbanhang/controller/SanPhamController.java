package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.SanPhamDTO;
import com.duyhk.bewebbanhang.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/san-pham") // http://localhost:8080/api/san-pham
@RequiredArgsConstructor
public class SanPhamController {
    private final SanPhamService sanPhamService;

    @GetMapping
    public  ResponseEntity<List<SanPhamDTO>> getAll(){
        return ResponseEntity.ok(sanPhamService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> create(@ModelAttribute SanPhamDTO sanPhamDTO) throws IOException {
        return  ResponseEntity.ok(sanPhamService.create(sanPhamDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@ModelAttribute SanPhamDTO sanPhamDTO, @PathVariable Long id) throws IOException {
        return  ResponseEntity.ok(sanPhamService.update(sanPhamDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(sanPhamService.delete(id));
    }
}
