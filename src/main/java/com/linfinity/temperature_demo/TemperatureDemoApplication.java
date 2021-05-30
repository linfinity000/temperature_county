package com.linfinity.temperature_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class TemperatureDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemperatureDemoApplication.class, args);
    }

}
