package com.duyhk.bewebbanhang.dto;

public enum TrangThai {
    DANGCHO(1),
    DANGTHU(2),
    HOANTHANH(3);

    public int status;

    TrangThai(int status) {
        this.status = status;
    }
}
