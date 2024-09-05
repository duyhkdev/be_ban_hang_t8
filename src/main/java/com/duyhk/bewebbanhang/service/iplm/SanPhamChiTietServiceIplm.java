package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.dto.SanPhamChiTietDTO;
import com.duyhk.bewebbanhang.entity.KichCo;
import com.duyhk.bewebbanhang.entity.MauSac;
import com.duyhk.bewebbanhang.entity.SanPham;
import com.duyhk.bewebbanhang.entity.SanPhamChiTiet;
import com.duyhk.bewebbanhang.repository.KichCoRepository;
import com.duyhk.bewebbanhang.repository.MauSacRepository;
import com.duyhk.bewebbanhang.repository.SanPhamChiTietRepository;
import com.duyhk.bewebbanhang.repository.SanPhamRepository;
import com.duyhk.bewebbanhang.service.SanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamChiTietServiceIplm implements SanPhamChiTietService {
    private final SanPhamChiTietRepository sanPhamChiTietRepo;
    private final MauSacRepository mauSacRepo;
    private final KichCoRepository kichCoRepo;
    private final SanPhamRepository sanPhamRepo;
    private final ModelMapper modelMapper;

    @Override
    public ResponseDTO<List<SanPhamChiTietDTO>> getAll(Integer page, Integer size) {
        Page<SanPhamChiTiet> entities = sanPhamChiTietRepo.findAll(PageRequest.of(page, size));
//        List<SanPhamChiTietDTO> list = entities.stream().map(x -> modelMapper.map(x, SanPhamChiTietDTO.class)).collect(Collectors.toList());
        List<SanPhamChiTietDTO> list = new ArrayList<>();
        for (SanPhamChiTiet entity : entities.getContent()) {
            SanPhamChiTietDTO dto = modelMapper.map(entity, SanPhamChiTietDTO.class);
            list.add(dto);
        }
        return ResponseDTO.<List<SanPhamChiTietDTO>>builder()
                .status(200)
                .data(list)
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .build();
    }

    @Override
    public SanPhamChiTietDTO findById(Long id) {
        return null;
    }

    @Override
    public String create(SanPhamChiTietDTO dto) {
        SanPhamChiTiet entity = modelMapper.map(dto, SanPhamChiTiet.class);
        mapToSave(dto, entity);
        sanPhamChiTietRepo.save(entity);
        return "Tao thanh cong";
    }

    private void mapToSave(SanPhamChiTietDTO dto, SanPhamChiTiet entity) {
        SanPham sanPham = sanPhamRepo.findById(dto.getSanPham().getId())
                .orElseThrow(() -> new RuntimeException("San pham not found"));
        KichCo kichCo = kichCoRepo.findById(dto.getKichCo().getId())
                .orElseThrow(() -> new RuntimeException("Kich co not found"));
        MauSac mauSac = mauSacRepo.findById(dto.getMauSac().getId())
                .orElseThrow(() -> new RuntimeException("Mau sac not found"));
        entity.setSanPham(sanPham);
        entity.setKichCo(kichCo);
        entity.setMauSac(mauSac);
        entity.setTen(sanPham.getTen());
        entity.setSoLuongDaBan(0l);
        //san pham

        sanPham.setSoLuongTonKho(dto.getSoLuongTonKho());
        Long giaThapNhat =
                sanPham.getGia() < dto.getGia() && sanPham.getGia() != 0
                        ? sanPham.getGia()
                        : dto.getGia();
        sanPham.setGia(giaThapNhat);

        sanPhamRepo.save(sanPham);
    }

    @Override
    public String update(SanPhamChiTietDTO dto, Long id) {
        SanPhamChiTiet entity = sanPhamChiTietRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not founnd"));
        maptoUpdate(dto, entity);
        sanPhamChiTietRepo.save(entity);
        return "Sửa thành công";
    }

    private void maptoUpdate(SanPhamChiTietDTO dto, SanPhamChiTiet entity) {
        SanPham sanPham = sanPhamRepo.findById(dto.getSanPham().getId())
                .orElseThrow(() -> new RuntimeException("San pham not found"));
        KichCo kichCo = kichCoRepo.findById(dto.getKichCo().getId())
                .orElseThrow(() -> new RuntimeException("Kich co not found"));
        MauSac mauSac = mauSacRepo.findById(dto.getMauSac().getId())
                .orElseThrow(() -> new RuntimeException("Mau sac not found"));
        entity.setSanPham(sanPham);
        entity.setKichCo(kichCo);
        entity.setMauSac(mauSac);
        entity.setTen(sanPham.getTen());
        entity.setGia(dto.getGia());
        entity.setTrangThai(dto.getTrangThai());

        Long soLuongTonKhoSanPham = sanPham.getSoLuongTonKho() - entity.getSoLuongTonKho() + dto.getSoLuongTonKho();

        sanPham.setSoLuongTonKho(soLuongTonKhoSanPham);

        entity.setSoLuongTonKho(dto.getSoLuongTonKho());


        Long giaThapNhat = sanPham.getGia() < dto.getGia() && sanPham.getGia() != 0
                ? sanPham.getGia()
                : dto.getGia();
        sanPham.setGia(giaThapNhat);

    }

    @Override
    public String delete(Long id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not founnd"));
        sanPhamChiTiet.setTrangThai(0);
        sanPhamChiTietRepo.save(sanPhamChiTiet);
        return "Xóa thành công";
    }
}
