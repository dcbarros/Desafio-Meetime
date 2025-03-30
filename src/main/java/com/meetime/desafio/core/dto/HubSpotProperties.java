package com.meetime.desafio.core.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "hubspot")
@Getter
@Setter
public class HubSpotProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUrl;
    private String authUrl;
    private String apiUrl;
    private String scopes;
}
