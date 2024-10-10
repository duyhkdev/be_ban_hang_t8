package com.duyhk.bewebbanhang.service;

import com.duyhk.bewebbanhang.dto.CapNhatSoLuongTQDTO;
import com.duyhk.bewebbanhang.dto.ThanhToanTaiQuayDTO;
import com.duyhk.bewebbanhang.dto.ThemVaoHoaDonRequest;

public interface BanHangTaiQuayService {
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
    String taoHoaDon();
    String themSanPhamVaoHoaDon(ThemVaoHoaDonRequest request);
    String capNhatSoLuongSanPham(CapNhatSoLuongTQDTO capNhatSoLuongTQDTO);
    String xoaSanPhamKhoiTaoHoaDon(Long id);
    String huyHoaDon(Long hoaDonId);
    String thanhToan(ThanhToanTaiQuayDTO request);
}
