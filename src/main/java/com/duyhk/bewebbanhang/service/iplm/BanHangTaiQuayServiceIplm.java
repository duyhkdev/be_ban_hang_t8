package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.CapNhatSoLuongTQDTO;
import com.duyhk.bewebbanhang.dto.ThanhToanTaiQuayDTO;
import com.duyhk.bewebbanhang.dto.ThemVaoHoaDonRequest;
import com.duyhk.bewebbanhang.dto.TrangThai;
import com.duyhk.bewebbanhang.entity.*;
import com.duyhk.bewebbanhang.repository.*;
import com.duyhk.bewebbanhang.service.BanHangTaiQuayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BanHangTaiQuayServiceIplm implements BanHangTaiQuayService {
    /*
     * TODO: Implement API for managing orders from online shopping carts.
     *  Tạo hoa don (tao hoa don cho trong bang hoa don)
     *  => thêm các sp vao hoa don (tao ban ghi trong bang hoa don chi tiet)
     *  => update lại so luong cua thang san_pham và san_pham_chi_tiet
     *  => thanh toan => chuyen trang thai
     *  Sua so luong
     *  - update lai so luong va thanh tien cua san pham trong hoa don chi tiet và hoa don
     *  - update lai so luong cua san pham va san pham chi tiet
     *  Huy hoa don
     *  => chuyen trang thai sang 0
     *  => neu ma trong hoa don co hoa don chi tiet (tuc la co san pham trong hoa don) thì nó cộng lại số lương
     */
    private final HoaDonRepo hoaDonRepo;
    private final HoaDonChiTietRepo hoaDonChiTietRepo;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;
    private final SanPhamRepository sanPhamRepo;
    private final TaiKhoanRepo taiKhoanRepo;
    private final GioHangRepo gioHangRepo;

    @Override
    public String taoHoaDon() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(TrangThai.DANG_CHO.status); // Trang thai dang mua hang
        String maDonHang = taoMaHoaDon();
        HoaDon checkExist = hoaDonRepo.findByMaHoaDon(maDonHang)
                .orElse(null);
        if (checkExist != null) {
            // bao loi
            throw new RuntimeException("Ma hoa don da ton tai");
        }
        hoaDon.setMaHoaDon(maDonHang);
        hoaDon.setTongSoSanPham(0l);
        hoaDon.setTongSoTien(0l);
        hoaDon.setNgayTao(LocalDate.now());
        hoaDon.setLoaiHoaDon(2);
        hoaDonRepo.save(hoaDon);
        return "Tao hoa don thanh cong";
    }

    /*
     *  => thêm các sp vao hoa don (tao ban ghi trong bang hoa don chi tiet)
     *  => update lại so luong cua thang san_pham và san_pham_chi_tiet
     *  => thanh toan => chuyen trang thai
     */
    @Override
    public String themSanPhamVaoHoaDon(ThemVaoHoaDonRequest request) {
        HoaDon hoaDon = hoaDonRepo.findById(request.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Hoa don khong ton tai"));
        if (hoaDon.getTrangThai() == TrangThai.DA_HUY.status) {
            throw new RuntimeException("Hoa don da duoc huy");
        }
        if (hoaDon.getLoaiHoaDon() == 1) {
            throw new RuntimeException("Hoa don khong hop le");
        }
        SanPhamChiTiet spct = sanPhamChiTietRepo.findById(request.getIdSpct())
                .orElseThrow(() -> new RuntimeException("San pham chi tiet khong ton tai"));

        if (spct.getSoLuongTonKho() < request.getSoLuong()) {
            throw new RuntimeException("So luong san pham khong du");
        }


        // set thong tin hoa don chi tiet
        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setDonGia(spct.getGia());

        hoaDonChiTiet.setSanPhamChiTiet(spct);
        hoaDonChiTiet.setHoaDon(hoaDon);

        // update lai so luong san pham va san pham chi tiet
        // san pham
        SanPham sanPham = spct.getSanPham();
        sanPham.setSoLuongTonKho(sanPham.getSoLuongTonKho() - request.getSoLuong());

        // san pham chi tiet
        SanPhamChiTiet sanPhamChiTietUpdate = spct.builder()
                .soLuongTonKho(spct.getSoLuongTonKho() - request.getSoLuong())
                .build();
        HoaDonChiTiet isExist = hoaDonChiTietRepo.findByHoaDonIdAndSanPhamChiTietId(hoaDon.getId(), spct.getId())
                .orElse(null);
        if (isExist == null) {
            hoaDonChiTiet.setSoLuong(request.getSoLuong());
            hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getDonGia() * hoaDonChiTiet.getSoLuong());
            // set thong tin hoa don
            hoaDon.setTongSoTien(hoaDon.getTongSoTien() + hoaDonChiTiet.getThanhTien());
            hoaDon.setTongSoSanPham(hoaDon.getTongSoSanPham() + 1);
        } else {
            hoaDonChiTiet.setSoLuong(isExist.getSoLuong() + request.getSoLuong());
            hoaDonChiTiet.setThanhTien(isExist.getDonGia() * isExist.getSoLuong());
            // update lai tong so tien
            hoaDon.setTongSoTien(hoaDon.getTongSoTien() + (hoaDonChiTiet.getThanhTien() - isExist.getThanhTien()));
        }


        // luu lai du lieu
        sanPhamChiTietRepo.save(sanPhamChiTietUpdate);
        sanPhamRepo.save(sanPham);
        hoaDonRepo.save(hoaDon);
        hoaDonChiTietRepo.save(hoaDonChiTiet);
        return "Them san pham thanh cong";
    }

    @Override
    public String capNhatSoLuongSanPham(CapNhatSoLuongTQDTO dto) {
        HoaDonChiTiet hdct = hoaDonChiTietRepo.findById(dto.getCthdId())
                .orElseThrow(() -> new RuntimeException("Chi tiet hoa don khong ton tai"));
        if (hdct.getHoaDon().getTrangThai() == TrangThai.DA_HUY.status) {
            throw new RuntimeException("Hoa don da bi huy");
        }
        SanPhamChiTiet spct = hdct.getSanPhamChiTiet();
        SanPham sp = spct.getSanPham();
        // tu 3 -> 1
        /*
            lay so luong ton kho + so luong cu - soluongmoi
         */
        // can tru so luong cu + so luong moi
        spct.setSoLuongTonKho(spct.getSoLuongTonKho() + hdct.getSoLuong() - dto.getSoLuong());
        sp.setSoLuongTonKho(sp.getSoLuongTonKho() + hdct.getSoLuong() - dto.getSoLuong());

        Long thanhTienMoi = hdct.getDonGia() * dto.getSoLuong();

        HoaDon hd = hdct.getHoaDon();
        hd.setTongSoTien(hd.getTongSoTien() - hdct.getThanhTien() + thanhTienMoi);

        hdct.setThanhTien(thanhTienMoi);
        hdct.setSoLuong(dto.getSoLuong());


        sanPhamRepo.save(sp);
        sanPhamChiTietRepo.save(spct);
        hoaDonChiTietRepo.save(hdct);
        hoaDonRepo.save(hd);
        return "Cap nhat so luong thanh cong";
    }

    @Override
    public String xoaSanPhamKhoiTaoHoaDon(Long id) {
        HoaDonChiTiet hdct = hoaDonChiTietRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chi tiet hoa don khong ton tai"));
        if (hdct.getHoaDon().getTrangThai() == TrangThai.DA_HUY.status) {
            throw new RuntimeException("Hoa don da bi huy");
        }
        // trang thai dh khac dang cho thi khong cho xoa

        HoaDon hoaDon = hdct.getHoaDon();
        SanPhamChiTiet spct = hdct.getSanPhamChiTiet();
        SanPham sp = spct.getSanPham();

        hoaDon.setTongSoSanPham(hoaDon.getTongSoSanPham() - 1);
        hoaDon.setTongSoTien(hoaDon.getTongSoTien() - hdct.getThanhTien());

        sp.setSoLuongTonKho(sp.getSoLuongTonKho() + hdct.getSoLuong());
        spct.setSoLuongTonKho(sp.getSoLuongTonKho() + hdct.getSoLuong());

        sanPhamRepo.save(sp);
        sanPhamChiTietRepo.delete(spct);
        hoaDonRepo.save(hoaDon);

        hoaDonChiTietRepo.deleteById(id);

        return "Xóa sản phâ thành công";
    }

    @Override
    public String huyHoaDon(Long hoaDonId) {
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hoa don khong ton tai"));
        if (hoaDon.getTrangThai() == TrangThai.DA_HUY.status) {
            throw new RuntimeException("Hoa don da bi huy");
        }
        if (hoaDon.getTrangThai() == TrangThai.HOAN_THANH.status) {
            throw new RuntimeException("Hoa don da thanh toan");
        }
        ///

        hoaDon.setTrangThai(TrangThai.DA_HUY.status);
        List<HoaDonChiTiet> hdcps = hoaDonChiTietRepo.findByHoaDonId(hoaDonId)
                .orElse(new ArrayList<>());

        for (HoaDonChiTiet x : hdcps) {
            SanPhamChiTiet spct = x.getSanPhamChiTiet();
            SanPham sp = spct.getSanPham();

            sp.setSoLuongTonKho(sp.getSoLuongTonKho() + x.getSoLuong());
            spct.setSoLuongTonKho(sp.getSoLuongTonKho() + x.getSoLuong());

            sanPhamRepo.save(sp);
            sanPhamChiTietRepo.save(spct);
        }
        hoaDonRepo.save(hoaDon);
        return "Huy hoa don thanh cong";
    }

    @Override
    public String thanhToan(ThanhToanTaiQuayDTO request) {
        HoaDon hoaDon = hoaDonRepo.findById(request.getHoaDonId())
                .orElseThrow(() -> new RuntimeException("Hoa don khong ton tai"));
        checkStatus(hoaDon.getTrangThai());

        hoaDon.setTrangThai(TrangThai.HOAN_THANH.status);
        hoaDon.setNgayHoanThanh(LocalDate.now());

        List<HoaDonChiTiet> hdcps = hoaDonChiTietRepo.findByHoaDonId(request.getHoaDonId())
                .orElse(new ArrayList<>());

        List<SanPham> sanPhamList = new ArrayList<>();
        List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();

        for (HoaDonChiTiet x : hdcps) {
            SanPhamChiTiet spct = x.getSanPhamChiTiet();
            SanPham sp = spct.getSanPham();

            sp.setSoLuongDaBan(sp.getSoLuongDaBan() + x.getSoLuong());
            spct.setSoLuongDaBan(sp.getSoLuongDaBan() + x.getSoLuong());

            sanPhamList.add(sp);
            sanPhamChiTietList.add(spct);
        }

        String soDienThoai = request.getSoDienThoai();
        if (soDienThoai == null) {
            hoaDon.setHoVaTen("CUSTOMER");
        } else {
            TaiKhoan taiKhoan = taiKhoanRepo.findBySoDienThoai(soDienThoai)
                    .orElse(new TaiKhoan());
            if (taiKhoan.getId() == null) {
                // tao tai khoan va gio hang neu tai khoan chua ton tai
                taoTaiKhoan(hoaDon.getTongSoTien(), taiKhoan, soDienThoai);
            }else {
                taiKhoan.setTongHoaDon(taiKhoan.getTongHoaDon() + 1);
                taiKhoan.setTongTien(taiKhoan.getTongTien() + hoaDon.getTongSoTien());
                boolean checkType = (taiKhoan.getTongHoaDon() >= 20
                        || taiKhoan.getTongTien() >= 20000000);
                taiKhoan.setHangTaiKhoan(checkType ? 2 : 1);
                taiKhoanRepo.save(taiKhoan);
            }
            hoaDon.setHoVaTen(taiKhoan.getHoVaTen());
            hoaDon.setTaiKhoan(taiKhoan);
            hoaDon.setSoDienThoai(soDienThoai);
        }
        sanPhamRepo.saveAll(sanPhamList);
        sanPhamChiTietRepo.saveAll(sanPhamChiTietList);
        hoaDonRepo.save(hoaDon);
        return "Thanh toan thanh cong";
    }

    private void taoTaiKhoan(Long tongSoTien, TaiKhoan taiKhoan, String soDienThoai) {
        taiKhoan.setSoDienThoai(soDienThoai);
        taiKhoan.setRole(Role.KHACHHANG);
        taiKhoan.setHoVaTen(soDienThoai);
        taiKhoan.setTongHoaDon(1l);
        taiKhoan.setTongTien(tongSoTien);
        taiKhoan.setHangTaiKhoan(1);
        taiKhoan.setTrangThai(1);
        taiKhoan = taiKhoanRepo.save(taiKhoan);

        GioHang gioHang = new GioHang();
        gioHang.setTaiKhoan(taiKhoan);
        gioHang.setTongSoTien(0l);
        gioHang.setTongSoSanPham(0l);
        gioHangRepo.save(gioHang);
    }

    private void checkStatus(Integer status) {
        if (status == TrangThai.DA_HUY.status) {
            throw new RuntimeException("Hoa don da bi huy");
        }///
        if (status == TrangThai.HOAN_THANH.status) {
            throw new RuntimeException("Hoa don da thanh toan");
        }
        //
    }

    private String taoMaHoaDon() {
        String arr = "0123456789";

        return "";
    }
}
