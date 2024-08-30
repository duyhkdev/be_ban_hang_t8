package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.SanPhamDTO;
import com.duyhk.bewebbanhang.entity.LoaiSanPham;
import com.duyhk.bewebbanhang.entity.SanPham;
import com.duyhk.bewebbanhang.repository.LoaiSanPhamRepository;
import com.duyhk.bewebbanhang.repository.SanPhamRepository;
import com.duyhk.bewebbanhang.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamServiceIplm implements SanPhamService {

    private final SanPhamRepository sanPhamRepo;
    private final ModelMapper mapper;
    private final LoaiSanPhamRepository loaiSanPhamRepo;
    private final CloudinaryServiceIplm cloudinaryService;

    @Override
    public List<SanPhamDTO> getAll() {
        List<SanPham> listEntities = sanPhamRepo.findAll();
        List<SanPhamDTO> listDtos = new ArrayList<>();
        for (SanPham entity : listEntities) {
            SanPhamDTO dto = mapper.map(entity, SanPhamDTO.class);
            listDtos.add(dto);
        }
        return listDtos;
    }

    @Override
    public SanPhamDTO findById(Long id) {
        SanPham entity = sanPhamRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Khong tim thay san pham")
        );
        return mapper.map(entity, SanPhamDTO.class);
    }

    @Override
    public String create(SanPhamDTO dto) throws IOException {
        List<String> images;
        if (dto.getFiles() != null) {
            images = processFilesCloud(dto.getFiles());
        } else {
            throw new RuntimeException("Vui long chon file");
        }
        SanPham sanPham = new SanPham();
        sanPham.setImages(images);
        sanPham.setSoLuongDaBan(0l);
        sanPham.setSoLuongTonKho(0l);
        sanPham.setGia(0l);
        mapToEntitySave(sanPham, dto);
        sanPhamRepo.save(sanPham);
        return "Thêm thành công";
    }


    @Override
    public String update(SanPhamDTO dto, Long id) throws IOException {
        SanPham sanPham = sanPhamRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy sản phẩm")
        );
        List<String> images;
        if (dto.getFiles() != null) {
            images = processFiles(dto.getFiles());
            sanPham.setImages(images);
        }
        mapToEntitySave(sanPham, dto);
        sanPhamRepo.save(sanPham);
        return "Sửa thành công";
    }

    @Override
    public String delete(Long id) {
        sanPhamRepo.deleteById(id);
        return "Xóa thành công";
    }

    private void mapToEntitySave(SanPham entity, SanPhamDTO dto) {
        entity.setMa(dto.getMa());
        entity.setTen(dto.getTen());
        entity.setMoTa(dto.getMoTa());
        entity.setTrangThai(dto.getTrangThai());
        // process add loai san pham
        Long loaiSanPhamId = dto.getLoaiSanPham().getId();
        LoaiSanPham lsp = loaiSanPhamRepo.findById(loaiSanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
        entity.setLoaiSanPham(lsp);
    }

    private List<String> processFilesCloud(List<MultipartFile> files) {
        List<String> images = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : files) {
                String url = cloudinaryService.uploadFile(multipartFile, "images");
                if (url == null) {
                    throw new RuntimeException("Upload file failed");
                }
                images.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return images;
    }

    private List<String> processFiles(List<MultipartFile> files) throws IOException {
        List<String> images = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String name = multipartFile.getOriginalFilename();
            images.add(name);
            String path = "D:/workspace/std/images";
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(path + "/" + name);
            multipartFile.transferTo(file);
        }
        return images;
    }
}
