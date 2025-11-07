package com.contenedores.operaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.contenedores.operaciones.model")
@EnableJpaRepositories("com.contenedores.operaciones.repository")
public class OperacionesApplication {

        public static void main(String[] args) {
                SpringApplication.run(OperacionesApplication.class, args);
        }

}