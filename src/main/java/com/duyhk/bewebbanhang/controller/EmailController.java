package com.duyhk.bewebbanhang.controller;

import com.duyhk.bewebbanhang.service.iplm.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/send")
    public String send(@RequestParam String email, @RequestParam String body) {
        emailService.sendEmail(email, "hello", body);
        return "Gui thanh cong";
    }
}
