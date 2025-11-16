package com.contenedores.apigateway.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    private boolean enabled = true;
    private List<String> acceptedIssuers = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAcceptedIssuers() {
        return acceptedIssuers;
    }

    public void setAcceptedIssuers(List<String> acceptedIssuers) {
        this.acceptedIssuers = acceptedIssuers;
    }
}
