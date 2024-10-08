package com.duyhk.bewebbanhang.service.iplm;

import com.duyhk.bewebbanhang.dto.CapNhatSoLuongTQDTO;
import com.duyhk.bewebbanhang.dto.ThemVaoHoaDonRequest;
import com.duyhk.bewebbanhang.dto.TrangThai;
import com.duyhk.bewebbanhang.entity.*;
import com.duyhk.bewebbanhang.repository.HoaDonChiTietRepo;
import com.duyhk.bewebbanhang.repository.HoaDonRepo;
import com.duyhk.bewebbanhang.repository.SanPhamChiTietRepository;
import com.duyhk.bewebbanhang.repository.SanPhamRepository;
import com.duyhk.bewebbanhang.service.BanHangTaiQuayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        spct.setSoLuongTonKho(spct.getSoLuongTonKho() +hdct.getSoLuong() - dto.getSoLuong());
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
    public String xoaSanPhamKhoiTaoHoaDon() {
        return null;
    }

    @Override
    public String huyHoaDon() {
        return null;
    }

    private String taoMaHoaDon() {
        String arr = "0123456789";

        return "";
    }
}
