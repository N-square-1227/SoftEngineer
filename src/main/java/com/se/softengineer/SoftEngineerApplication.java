package com.se.softengineer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.se.softengineer.dao")
public class SoftEngineerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftEngineerApplication.class, args);
    }

}
