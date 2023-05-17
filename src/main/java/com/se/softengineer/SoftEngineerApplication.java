package com.se.softengineer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.se.softengineer.mapper")
public class SoftEngineerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftEngineerApplication.class, args);
    }

}
