package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.MauSacDTO;
import com.duyhk.bewebbanhang.entity.MauSac;
import com.duyhk.bewebbanhang.repository.MauSacRepository;
import com.duyhk.bewebbanhang.service.IMauSacService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MauSacServiceIplm implements IMauSacService {

    private final MauSacRepository mauSacRepo;

    @Override
    public List<MauSacDTO> getAll() {
        List<MauSac> listEntities = mauSacRepo.findAll();
        List<MauSacDTO> listDtos = new ArrayList<MauSacDTO>();
        for (MauSac entity : listEntities) {
            MauSacDTO dto = MauSacDTO.builder()
                    .id(entity.getId())
                    .ten(entity.getTen())
                    .build();
            listDtos.add(dto);
        }
        return listDtos;
    }

    @Override
    public MauSacDTO findById(Long id) {
        MauSac entity = mauSacRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Mau sac id: " + id + " not found.")
        );
        return MauSacDTO.builder()
                .id(entity.getId())
                .ten(entity.getTen())
                .build();
    }

    @Override
    public String create(MauSacDTO dto) {
        MauSac entity = MauSac.builder()
                .ten(dto.getTen())
                .build();
        mauSacRepo.save(entity);
        return "Thêm thành công";
    }

    @Override
    public String update(MauSacDTO dto, Long id) {
        MauSac entity = mauSacRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Mau sac id: " + id + " not found.")
        );
        entity = MauSac.builder()
                .id(entity.getId())
                .ten(dto.getTen())
                .build();
        mauSacRepo.save(entity);
        return "Sửa thành công";
    }

    @Override
    public String delete(Long id) {
        mauSacRepo.deleteById(id);
        return "Xóa thành công";
    }
}
