package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.MauSacDTO;
import com.duyhk.bewebbanhang.entity.MauSac;

import java.util.List;

public interface IMauSacService {
    List<MauSacDTO> getAll();
    MauSacDTO findById(Long id);
    String create(MauSacDTO dto);
    String update(MauSacDTO dto, Long id);
    String delete(Long id);
}
