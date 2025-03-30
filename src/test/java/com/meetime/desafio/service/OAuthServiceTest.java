package com.meetime.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;

import com.meetime.desafio.core.dto.HubSpotProperties;
import com.meetime.desafio.infrastructure.hubspot.TokenManager;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class OAuthServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private HubSpotProperties hubSpotProperties;

    @InjectMocks
    private OAuthService oAuthService;

    @Test
    void getAccessToken_DeveRetornarTokenEAgravar() {

        String code = "codigo-de-teste";
        String accessToken = "token-accesso-123";
        String refreshToken = "token-refresh-456";


        when(hubSpotProperties.getTokenUrl()).thenReturn("https://api.hubapi.com/oauth/v1/token");
        when(hubSpotProperties.getClientId()).thenReturn("client-id");
        when(hubSpotProperties.getClientSecret()).thenReturn("client-secret");
        when(hubSpotProperties.getRedirectUri()).thenReturn("http://localhost:8080/oauth/callback");


        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        Map<String, String> respostaToken = Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken
        );

        when(responseSpec.bodyToMono(any(Class.class))).<Map<String, String>>thenReturn(Mono.just(respostaToken));

        String resultado = oAuthService.getAccessToken(code).block();

        verify(tokenManager).saveTokens(accessToken, refreshToken);
        assertEquals(accessToken, resultado);
    }


}
