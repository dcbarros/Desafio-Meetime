package com.meetime.desafio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import com.meetime.desafio.infrastructure.hubspot.TokenManager;


@Configuration
public class HubSpotClientConfig {

    @Value("${hubspot.api-url}")
    private String hubspotApiUrl;

    @Bean
    public WebClient hubspotWebClient(TokenManager tokenManager) {

        return WebClient.builder()
                .baseUrl(hubspotApiUrl)
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