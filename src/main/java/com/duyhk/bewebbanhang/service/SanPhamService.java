package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.SanPhamDTO;

import java.io.IOException;
import java.util.List;

public interface SanPhamService {
    List<SanPhamDTO> getAll();
    SanPhamDTO findById(Long id);
    String create(SanPhamDTO dto) throws IOException;
    String update(SanPhamDTO dto, Long id) throws IOException;
    String delete(Long id);
}
