package ru.github.happshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HappShopApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HappShopApplication.class, args);
    }
}
