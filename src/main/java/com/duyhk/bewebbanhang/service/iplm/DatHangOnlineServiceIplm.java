package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.*;
import com.duyhk.bewebbanhang.entity.*;
import com.duyhk.bewebbanhang.repository.*;
import com.duyhk.bewebbanhang.service.DatHangOnlineService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatHangOnlineServiceIplm implements DatHangOnlineService {
    private final HoaDonRepo hoaDonRepo;
    private final HoaDonChiTietRepo hoaDonChiTietRepo;
    private final TaiKhoanRepo taiKhoanRepo;
    private final GioHangRepo gioHangRepo;
    private final GioHangChiTietRepo gioHangChiTietRepo;
    private final SanPhamRepository sanPhamRepo;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;
    private final ModelMapper modelMapper;

    @Override
    public ThongTinHoaDonDTO getThongTinHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hoa don khong ton tai"));
        List<HoaDonChiTiet> listHoaDon = hoaDonChiTietRepo.findByHoaDonId(hoaDon.getId())
                .orElseThrow(() -> new RuntimeException("Hoa don khong ton tai"));
        ThongTinHoaDonDTO thongTin = new ThongTinHoaDonDTO();
        mapToDTO(hoaDon, listHoaDon, thongTin);
        return thongTin;
    }

    private void mapToDTO(HoaDon hoaDon, List<HoaDonChiTiet> list, ThongTinHoaDonDTO thongTin) {
        HoaDonDTO hoaDonDTO = modelMapper.map(hoaDon, HoaDonDTO.class);
        thongTin.setThongTinChung(hoaDonDTO);
        // set tt chi tiet
        List<HoaDonChiTietDTO> listDto = new ArrayList<>();
        for (HoaDonChiTiet x : list) {
            HoaDonChiTietDTO hdct = modelMapper.map(x, HoaDonChiTietDTO.class);
            listDto.add(hdct);
        }
        thongTin.setThongTinChiTiet(listDto);
    }

    /*
        Khi ma dat hang khong dang nhap
        - FE can tao 1 cai gio hang tam - co the luu o trong session, cookie, storage
        - Khi them san pham vao gio hang thi no se luu vao trong gio hang trong session, cookie, storage
        {
            gioHangChiTietDTOList: [
                {
                    id: 1,
                    tenSanPham: "San pham 1",
                    soLuong: 1,
                    gia: 10000
                },
                {
                    id: 1,
                    tenSanPham: "San pham 2",
                    soLuong: 1,
                    gia: 10000
                },
                {
                    id: 1,
                    tenSanPham: "San pham 3",
                    soLuong: 1,
                    gia: 10000
                }
            ]
        }
     */
    @Override
    public String datHang(ThongTinDatHangDTO dto) {
        /*
            - Kiem tra dang nhap
            - Nhap day du thong tin khach hang
            - Kiem tra so luong san pham con khong
            - Tao hoa don
            - Xoa het san pham trong gio hang => tao ban ghi hoa don chi tiet
            - Update lai so luong cua san pham (soLuongTonKho, soLuongDaBan)
            - Tich diem khach hang
         */
        //
        TaiKhoan taiKhoan = taiKhoanRepo.findById(dto.getTaiKhoanId())
                .orElseThrow(() -> new RuntimeException("Tai khoan khong ton tai"));
        String pattern = "^(0|84)(2(0[3-9]|1[0-689]|2[0-25-9]|3[2-9]|4[0-9]|5[124-9]|6[0369]|7[0-7]|8[0-9]|9[012346789])|3[2-9]|5[25689]|7[06-9]|8[0-9]|9[012346789])([0-9]{7})$";
        if (!dto.getSoDienThoai().matches(pattern)) {
            throw new RuntimeException("Invalid pattern");
        }
        GioHang gioHang = gioHangRepo.findByTaiKhoanId(dto.getTaiKhoanId())
                .orElseThrow(() -> new RuntimeException("Invalid"));
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepo.findByGioHangId(gioHang.getId())
                .orElseThrow(() -> new RuntimeException("Vui long them sp vao gio hang"));

        //tao hoa don
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(1);
        hoaDon.setTaiKhoan(taiKhoan);
        hoaDon.setSoDienThoai(dto.getSoDienThoai());
        hoaDon.setHoVaTen(dto.getHoVaTen());
        hoaDon.setDiaChi(dto.getDiaChi());
        hoaDon.setNgayTao(LocalDate.now());
        hoaDon = hoaDonRepo.save(hoaDon);

        List<HoaDonChiTiet> listEntityHdct = new ArrayList<>();
        List<SanPhamChiTiet> listEntitySpct = new ArrayList<>();
        List<SanPham> listEntitySp = new ArrayList<>();

        Long thanhTien = 0l;
        Long soSanPham = 0l;
        for (GioHangChiTiet gioHangChiTiet : gioHangChiTietList) {
            SanPhamChiTiet spct = sanPhamChiTietRepo.checkSoLuongDatHang(gioHangChiTiet.getSoLuong(), gioHangChiTiet.getId())
                    .orElseThrow(() -> new RuntimeException("San pham so luong khong du"));
            HoaDonChiTiet hdct = new HoaDonChiTiet();
            hdct.setSoLuong(gioHangChiTiet.getSoLuong());
            hdct.setHoaDon(hoaDon);
            thanhTien += hdct.getSoLuong() * hdct.getDonGia();
            soSanPham += 1;
            hdct.setThanhTien(hdct.getSoLuong() * hdct.getDonGia());
            hdct.setSanPhamChiTiet(spct);
            listEntityHdct.add(hdct);

            spct.setSoLuongTonKho(spct.getSoLuongTonKho() - gioHangChiTiet.getSoLuong());
            spct.setSoLuongDaBan(spct.getSoLuongDaBan() + gioHangChiTiet.getSoLuong());
            listEntitySpct.add(spct);

            SanPham sanPham = spct.getSanPham();
            sanPham.setSoLuongTonKho(sanPham.getSoLuongTonKho() - gioHangChiTiet.getSoLuong());
            sanPham.setSoLuongDaBan(sanPham.getSoLuongDaBan() + gioHangChiTiet.getSoLuong());
            listEntitySp.add(sanPham);
        }
        gioHang.setTongSoTien(0l);
        gioHang.setTongSoSanPham(0l);

        hoaDon.setTongSoTien(thanhTien);
        hoaDon.setTongSoSanPham(soSanPham);

        gioHangRepo.save(gioHang);
        gioHangChiTietRepo.deleteAll(gioHangChiTietList);
        hoaDonChiTietRepo.saveAll(listEntityHdct);
        sanPhamRepo.saveAll(listEntitySp);
        sanPhamChiTietRepo.saveAll(listEntitySpct);
        hoaDonRepo.save(hoaDon);

//        sanPhamChiTietRepo.checkSoLuongDatHang(dto.get, )

        taiKhoan.setTongHoaDon(taiKhoan.getTongHoaDon() + 1);
        taiKhoan.setTongTien(taiKhoan.getTongTien() + thanhTien);
        boolean kiemTraTKV =  taiKhoan.getTongTien() > 10000000 && taiKhoan.getTongHoaDon() > 25;
        taiKhoan.setHangTaiKhoan(kiemTraTKV ? 2 : 1 );
        taiKhoanRepo.save(taiKhoan);
        return "Dat hang thanh cong";
    }

    @Override
    public String updateTrangThai(Long id, Integer trangThai) {
        HoaDon hoaDon = hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException(ExceptionEnum.HOA_DON_NOT_FOUND.name()));
        if(trangThai == 2 && hoaDon.getTrangThai() != 1){
            throw new RuntimeException("Hoa don da duoc thanh toan");
        }
        TrangThai.DANGCHO.
        if(trangThai == 3 && hoaDon.getTrangThai() != TrangThai.HOANTHANH.status){
            throw new RuntimeException("Hoa don da duoc in hoa don");
        }

        return null;
    }
}
