package com.meetime.desafio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import com.meetime.desafio.core.dto.HubSpotProperties;
import com.meetime.desafio.infrastructure.hubspot.TokenManager;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class HubSpotClientConfig {

    private final HubSpotProperties hubSpotProperties;

    @Bean
    public WebClient hubspotWebClient(TokenManager tokenManager) {

        return WebClient.builder()
                .baseUrl(hubSpotProperties.getApiUrl())
                .filter((request, next) -> {

                    if (tokenManager.hasTokens()) {
                        ClientRequest authorizedRequest = ClientRequest.from(request)
                                .headers(headers -> headers.setBearerAuth(tokenManager.getAccessToken()))
                                .build();
                        return next.exchange(authorizedRequest);
                    }
                    return next.exchange(request);
                })
                .build();
    }
}