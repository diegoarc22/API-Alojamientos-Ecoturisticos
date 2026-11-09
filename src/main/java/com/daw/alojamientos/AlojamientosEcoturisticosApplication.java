package com.daw.alojamientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AlojamientosEcoturisticosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlojamientosEcoturisticosApplication.class, args);
    }
}
