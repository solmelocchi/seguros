package com.example.seguros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SegurosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SegurosApplication.class, args);
    }
}
