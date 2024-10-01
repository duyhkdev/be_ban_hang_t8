package com.duyhk.bewebbanhang.dto;

public enum TrangThai {
    DA_HUY(0),
    DANG_CHO(1),
    CHO_LAY_HANG(2),
    DANG_GIAO_HANG(3),
    HOAN_THANH(4);

    public int status;

    TrangThai(int status) {
        this.status = status;
    }
}
