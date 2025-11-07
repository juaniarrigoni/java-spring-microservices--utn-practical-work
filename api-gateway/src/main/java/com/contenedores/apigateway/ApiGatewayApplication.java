package com.contenedores.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.contenedores.apigateway.config.GatewayProperties;

@SpringBootApplication
@EnableConfigurationProperties(GatewayProperties.class)
public class ApiGatewayApplication {
        public static void main(String[] args) {
                SpringApplication.run(ApiGatewayApplication.class, args);
        }
}
