package com.contenedores.solicitudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.contenedores.solicitudes.model") // Asegura que JPA escanee tus entidades
@EnableJpaRepositories("com.contenedores.solicitudes.repository") // Asegura que JPA escanee tus repositorios
public class SolicitudesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolicitudesApplication.class, args);
    }

}