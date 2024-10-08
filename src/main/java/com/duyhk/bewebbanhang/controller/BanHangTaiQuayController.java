package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.dto.ResponseDTO;
import com.duyhk.bewebbanhang.dto.ThemVaoHoaDonRequest;
import com.duyhk.bewebbanhang.service.BanHangTaiQuayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ban-hang-tai-quay")
@RequiredArgsConstructor
public class BanHangTaiQuayController {

    /*
     * TODO: Implement API for managing orders from online shopping carts.
     *  Tạo hoa don (tao hoa don cho trong bang hoa don)
     *  => thêm các sp vao hoa don (tao ban ghi trong bang hoa don chi tiet)
     *  => update lại so luong cua thang san_pham và san_pham_chi_tiet
     *  => thanh toan => chuyen trang thai
     *  Huy hoa don
     *  => chuyen trang thai sang 0
     *  => neu ma trong hoa don co hoa don chi tiet (tuc la co san pham trong hoa don) thì nó cộng lại số lương
     */
    private final BanHangTaiQuayService banHangTaiQuayService;

    @PostMapping("/tao-hoa-don")
    public ResponseDTO<Void> taoHoaDon(){
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(banHangTaiQuayService.taoHoaDon())
                .build();
    }
    @PostMapping("/them-vao-hoa-don")
    public ResponseDTO<Void> themSanPhamVaoHoaDon(@RequestBody ThemVaoHoaDonRequest request){
        return ResponseDTO.<Void>builder()
                .status(200)
                .message(banHangTaiQuayService.themSanPhamVaoHoaDon(request))
                .build();
    }
}
