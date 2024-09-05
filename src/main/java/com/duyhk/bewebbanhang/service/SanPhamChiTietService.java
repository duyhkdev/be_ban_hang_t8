package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.dto.SanPhamChiTietDTO;


import java.io.IOException;
import java.util.List;

public interface SanPhamChiTietService {

    ResponseDTO<List<SanPhamChiTietDTO>> getAll(Integer page, Integer size);
    SanPhamChiTietDTO findById(Long id);
    String create(SanPhamChiTietDTO dto);
    String update(SanPhamChiTietDTO dto, Long id);
    String delete(Long id);
}
