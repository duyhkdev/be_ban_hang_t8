package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.MauSacDTO;
import com.duyhk.bewebbanhang.entity.MauSac;
import com.duyhk.bewebbanhang.service.IMauSacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mau-sac") // http://localhost:8080/api/mau-sac
@RequiredArgsConstructor
public class MauSacController {
    private final IMauSacService mauSacService;

    @GetMapping
    public ResponseEntity<List<MauSacDTO>> getAll(){
        return ResponseEntity.ok(mauSacService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MauSacDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(mauSacService.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid MauSacDTO dto){
        return ResponseEntity.ok(mauSacService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody MauSacDTO dto){
        dto.setId(id);
        return ResponseEntity.ok(mauSacService.update(dto, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(mauSacService.delete(id));
    }
}
