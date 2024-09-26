package com.duyhk.bewebbanhang.dto;

public enum ExceptionEnum {
    HOA_DON_NOT_FOUND("Hoa don khong ton tai");
    String message;
    ExceptionEnum(String message) {
        this.message = message;
    }
}
