package com.duyhk.bewebbanhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeWebBanHangApplication {

    public static void main(String[] args) {
        //  đánh dấu no la 1 component <=>  1 bean
        // bean, spring container
        // các bean được quản lý bởi spring container
        SpringApplication.run(BeWebBanHangApplication.class, args);
    }

}
