package br.com.api.commerce.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

    @Value("${secret.token.jwt}")
    private String secretTokenJWT;

    public String getSecretTokenJWT() {
        return secretTokenJWT;
    }
}
