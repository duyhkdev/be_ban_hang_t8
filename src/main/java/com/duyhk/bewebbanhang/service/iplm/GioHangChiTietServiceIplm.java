package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.GioHangChiTietDTO;
import com.duyhk.bewebbanhang.entity.GioHang;
import com.duyhk.bewebbanhang.entity.GioHangChiTiet;
import com.duyhk.bewebbanhang.entity.SanPham;
import com.duyhk.bewebbanhang.entity.SanPhamChiTiet;
import com.duyhk.bewebbanhang.repository.GioHangChiTietRepo;
import com.duyhk.bewebbanhang.repository.GioHangRepo;
import com.duyhk.bewebbanhang.repository.SanPhamChiTietRepository;
import com.duyhk.bewebbanhang.repository.SanPhamRepository;
import com.duyhk.bewebbanhang.service.GioHangChiTietService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/gio-hang-chi-tiet")
public class GioHangChiTietServiceIplm implements GioHangChiTietService {
    private final GioHangRepo gioHangRepo;
    private final GioHangChiTietRepo gioHangChiTietRepo;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<GioHangChiTietDTO> getByGoHangId(Long id) {
        List<GioHangChiTiet> entities = gioHangChiTietRepo.findByGioHangId(id)
                .orElseThrow(() -> new RuntimeException(("Gio hang khong hop le")));
        List<GioHangChiTietDTO> dtos = entities.stream().map(x -> modelMapper.map(x, GioHangChiTietDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }

    @Override
    public String themVaoGioHang(GioHangChiTietDTO dto) {
        GioHangChiTiet entity = new GioHangChiTiet();
        GioHang gioHang = gioHangRepo.findById(dto.getGioHangId())
                .orElseThrow(() -> new RuntimeException("Gio hang khong ton tai"));
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(dto.getSanPhamChiTiet().getId())
                .orElseThrow(() -> new RuntimeException("San pham chi tiet khong ton tai"));
        GioHangChiTiet isExist = gioHangChiTietRepo.findBySanPhamChiTietIdAndGioHangId(sanPhamChiTiet.getId(), dto.getGioHangId())
                .orElse(null);
        if (isExist == null) {
            entity.setSoLuong(dto.getSoLuong());
            gioHang.setTongSoSanPham(gioHang.getTongSoSanPham() + 1);
            gioHangRepo.save(gioHang);
        } else {
            entity.setId(isExist.getId());
            entity.setSoLuong(isExist.getSoLuong() + dto.getSoLuong());
        }
        entity.setGioHang(gioHang);
        entity.setSanPhamChiTiet(sanPhamChiTiet);
        gioHangChiTietRepo.save(entity);


        return "Them thanh cong";
    }

    @Override
    public String suaSoLuong(Long soLuongMoi, Long id) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gio hang chi tiết đã bị xoa"));
        // neu co ton tai thi xu ly
        SanPhamChiTiet sanPhamChiTiet = gioHangChiTiet.getSanPhamChiTiet();
        if (sanPhamChiTiet.getSoLuongTonKho() < soLuongMoi) {
            throw new RuntimeException("So luong trong kho khong du");
        }
        gioHangChiTiet.setSoLuong(soLuongMoi);
        gioHangChiTietRepo.save(gioHangChiTiet);
        return "Sua thanh cong";
    }

    @Override
    public String xoaKhoiGioHang(Long id) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Gio hang chi tiết đã bị xoa"));
        // neu co ton tai thi xu ly
        GioHang gioHang = gioHangChiTiet.getGioHang();
        gioHang.setTongSoSanPham(gioHang.getTongSoSanPham() - 1);
        gioHangRepo.save(gioHang);
        gioHangChiTietRepo.deleteById(id);
        return "Xoa thanh cong";
    }
}
