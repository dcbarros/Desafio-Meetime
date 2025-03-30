package com.meetime.desafio.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meetime.desafio.service.OAuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/auth-url")
    public ResponseEntity<String> getAuthUrl() {
        String authUrl = oAuthService.getAuthorizationUrl();
        return ResponseEntity.ok(authUrl);
    }

    
    @GetMapping("/callback")
    public Mono<ResponseEntity<String>> callback(@RequestParam("code") String code) {
        return oAuthService.getAccessToken(code)
                .map(token -> ResponseEntity.ok("Token recebido e armazenado com sucesso"));
    }
}
