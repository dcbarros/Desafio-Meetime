package com.meetime.desafio.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.meetime.desafio.core.dto.HubSpotProperties;
import com.meetime.desafio.infrastructure.hubspot.TokenManager;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final WebClient webClient;  
    private final TokenManager tokenManager;
    private final HubSpotProperties hubSpotProperties;

    public Mono<String> getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", hubSpotProperties.getClientId());
        formData.add("client_secret", hubSpotProperties.getClientSecret());
        formData.add("redirect_uri", hubSpotProperties.getRedirectUri());
        formData.add("code", code);

        return webClient.post()
                .uri(hubSpotProperties.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(tokenMap -> {
                    String accessToken = (String) tokenMap.get("access_token");
                    String refreshToken = (String) tokenMap.get("refresh_token");
                    tokenManager.saveTokens(accessToken, refreshToken);
                })
                .map(tokenMap -> (String) tokenMap.get("access_token"));
    }

    public String getAuthorizationUrl() {
        return hubSpotProperties.getAuthUrl()
                + "?client_id=" + hubSpotProperties.getClientId()
                + "&redirect_uri=" + hubSpotProperties.getRedirectUri()
                + "&scope=" + hubSpotProperties.getScopes()
                + "&response_type=code";
    }
}
