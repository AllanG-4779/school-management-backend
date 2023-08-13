package com.user;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolManagementUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementUserApplication.class);
    }
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}