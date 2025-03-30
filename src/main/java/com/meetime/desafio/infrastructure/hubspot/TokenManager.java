package com.meetime.desafio.infrastructure.hubspot;

import org.springframework.stereotype.Component;

import lombok.Getter;

/** 
 * Estou fazendo isso para faciliar o teste, mas o ideal seria usar um banco de dados ou um serviço de cache.
 * 
*/
@Component
@Getter
public class TokenManager {
    
    private String accessToken;
    private String refreshToken;

    public void saveTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public boolean hasTokens() {
        return accessToken != null && !accessToken.isBlank();
    }
}
