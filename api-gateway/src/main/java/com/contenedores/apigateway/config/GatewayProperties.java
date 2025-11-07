package com.contenedores.apigateway.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tpi.gateway")
public class GatewayProperties {

    private Map<String, Service> services = new LinkedHashMap<>();

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    public static class Service {
        private String uri;
        private int stripPrefix = 2;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(int stripPrefix) {
            this.stripPrefix = stripPrefix;
        }
    }
}
