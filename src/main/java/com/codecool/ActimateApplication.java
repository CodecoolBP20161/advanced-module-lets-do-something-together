package com.codecool;

import com.codecool.config.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActimateApplication {

    private DataLoader dataLoader;

    public static void main(String[] args) {
        SpringApplication.run(ActimateApplication.class, args);
    }
}
