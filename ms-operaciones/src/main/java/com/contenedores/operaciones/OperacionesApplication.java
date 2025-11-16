package com.contenedores.operaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
@EntityScan("com.contenedores.operaciones.model")
@EnableJpaRepositories("com.contenedores.operaciones.repository")


@SecurityScheme(
        name = "Keycloak",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OperacionesApplication {

        public static void main(String[] args) {
                SpringApplication.run(OperacionesApplication.class, args);
        }

}