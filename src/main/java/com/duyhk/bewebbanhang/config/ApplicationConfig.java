package com.duyhk.bewebbanhang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
@Configuration
public class ApplicationConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
