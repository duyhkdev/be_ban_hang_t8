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
    public String datHang(ThongTinDatHangRequest dto) {
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

            updateSoLuongSanPham(gioHangChiTiet.getSoLuong(), gioHangChiTiet.getSanPhamChiTiet().getId(), listEntitySpct, listEntitySp);

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

        return "Dat hang thanh cong";
    }

    @Override
    public String datHangKhongDangNhap(ThongTinDatHangKhongDangNhapDTO dto) {
        List<GioHangChiTietDTO> listGioHangChitiet = dto.getListGioHang();

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setRole(Role.KHACHHANG);
        taiKhoan.setEmail(dto.getSoDienThoai());
        taiKhoan.setHoVaTen(dto.getSoDienThoai());
        taiKhoan.setTongTien(0l);
        taiKhoan.setTongHoaDon(0l);
        taiKhoan.setHangTaiKhoan(1);
        taiKhoan.setTrangThai(1);
        taiKhoan = taiKhoanRepo.save(taiKhoan);


        HoaDon hoaDon = new HoaDon();
        hoaDon.setHoVaTen(dto.getHoVaTen());
        hoaDon.setDiaChi(dto.getDiaChi());
        hoaDon.setSoDienThoai(dto.getSoDienThoai());
        hoaDon.setTaiKhoan(taiKhoan);
        hoaDon.setNgayTao(LocalDate.now());
        hoaDon = hoaDonRepo.save(hoaDon);

        List<HoaDonChiTiet> listHdct = new ArrayList<>();
        List<SanPhamChiTiet> listEntitySpct = new ArrayList<>();
        List<SanPham> listEntitySp = new ArrayList<>();

        Long thanhTien = 0l;
        Long soSanPham = 0l;

        for (GioHangChiTietDTO x : listGioHangChitiet) {
            updateSoLuongSanPham(x.getSoLuong(), x.getSanPhamChiTiet().getId(), listEntitySpct, listEntitySp);
            HoaDonChiTiet hdct = new HoaDonChiTiet();
            SanPhamChiTiet spct = sanPhamChiTietRepo.findById(x.getSanPhamChiTiet().getId())
                    .orElseThrow(() -> new RuntimeException("Sp không tồn tịa"));
            hdct.setSanPhamChiTiet(spct);
            hdct.setSoLuong(x.getSoLuong());
            hdct.setThanhTien(spct.getGia() * x.getSoLuong());
            hdct.setDonGia(spct.getGia());
            thanhTien += hdct.getSoLuong() * hdct.getDonGia();
            soSanPham += 1;
            hdct.setHoaDon(hoaDon);
            listHdct.add(hdct);
        }
        hoaDon.setTongSoTien(thanhTien);
        hoaDon.setTongSoSanPham(soSanPham);

        hoaDonRepo.save(hoaDon);
        hoaDonChiTietRepo.saveAll(listHdct);
        sanPhamChiTietRepo.saveAll(listEntitySpct);
        sanPhamRepo.saveAll(listEntitySp);

        return "Đặt hàng thành công";
    }

    private void updateSoLuongSanPham(Long soLuong, Long idSpct, List<SanPhamChiTiet> listEntitySpct, List<SanPham> listEntitySp) {
        SanPhamChiTiet spct = sanPhamChiTietRepo.findById(idSpct)
                .orElseThrow(() -> new RuntimeException("Sp không tồn tịa"));
        spct.setSoLuongTonKho(spct.getSoLuongTonKho() - soLuong);
        spct.setSoLuongDaBan(spct.getSoLuongDaBan() + soLuong);
        listEntitySpct.add(spct);

        SanPham sanPham = spct.getSanPham();
        sanPham.setSoLuongTonKho(sanPham.getSoLuongTonKho() - soLuong);
        sanPham.setSoLuongDaBan(sanPham.getSoLuongDaBan() + soLuong);
        listEntitySp.add(sanPham);
    }

    @Override
    public String updateTrangThai(Long id, Integer trangThai) {
        HoaDon hoaDon = hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException(ExceptionEnum.HOA_DON_NOT_FOUND.message));
        if ((trangThai == TrangThai.CHO_LAY_HANG.status && hoaDon.getTrangThai() != TrangThai.DANG_CHO.status)
                || (trangThai == TrangThai.DANG_GIAO_HANG.status && hoaDon.getTrangThai() != TrangThai.CHO_LAY_HANG.status)
                || (trangThai == TrangThai.HOAN_THANH.status && hoaDon.getTrangThai() != TrangThai.DANG_GIAO_HANG.status)
                || (trangThai == TrangThai.DA_HUY.status && hoaDon.getTrangThai() == TrangThai.HOAN_THANH.status)) {
            throw new RuntimeException("Hoa don da thay doi trang thai khac");
        }

        if (trangThai == TrangThai.DA_HUY.status) {
            huyHoaDon(hoaDon);
        } else if (trangThai == TrangThai.HOAN_THANH.status) {
            hoanThanhHoaDon(hoaDon);
        }

        return "Sửa thành công";
    }

    private void hoanThanhHoaDon(HoaDon hoaDon) {
        hoaDon.setTrangThai(TrangThai.HOAN_THANH.status);
        hoaDon.setNgayHoanThanh(LocalDate.now());
        hoaDonRepo.save(hoaDon);
        TaiKhoan taiKhoan = hoaDon.getTaiKhoan();
        taiKhoan.setTongHoaDon(taiKhoan.getTongHoaDon() + 1);
        taiKhoan.setTongTien(taiKhoan.getTongTien() + hoaDon.getTongSoTien());
        boolean kiemTraTKV = taiKhoan.getTongTien() > 10000000 && taiKhoan.getTongHoaDon() > 25;
        taiKhoan.setHangTaiKhoan(kiemTraTKV ? 2 : 1);
        taiKhoanRepo.save(taiKhoan);
    }

    private void huyHoaDon(HoaDon hoaDon) {
        hoaDon.setTrangThai(TrangThai.DA_HUY.status);
        List<HoaDonChiTiet> listHdct = hoaDonChiTietRepo.findByHoaDonId(hoaDon.getId())
                .orElse(null);
        if (listHdct == null) {
            hoaDonRepo.save(hoaDon);
            return;
        }
        hoaDonRepo.save(hoaDon);
        for (HoaDonChiTiet hdct : listHdct) {
            SanPhamChiTiet spct = hdct.getSanPhamChiTiet();
            SanPham sp = spct.getSanPham();
            spct.setSoLuongTonKho(spct.getSoLuongTonKho() + hdct.getSoLuong());
            spct.setSoLuongDaBan(spct.getSoLuongDaBan() - hdct.getSoLuong());
            sp.setSoLuongTonKho(sp.getSoLuongTonKho() + hdct.getSoLuong());
            sp.setSoLuongDaBan(sp.getSoLuongDaBan() - hdct.getSoLuong());
            sanPhamRepo.save(sp);
            sanPhamChiTietRepo.save(spct);
        }

    }
}
