package com.meetime.desafio.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.meetime.desafio.config.exceptions.ConflictException;
import com.meetime.desafio.config.exceptions.NotFoundException;
import com.meetime.desafio.config.exceptions.UnauthorizedException;
import com.meetime.desafio.infrastructure.hubspot.HubSpotClient;
import com.meetime.desafio.infrastructure.hubspot.TokenManager;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final HubSpotClient hubSpotClient;       
    private final TokenManager tokenManager;

    
    public Mono<String> createContact(Map<String, Object> contactProps) {

        if (!tokenManager.hasTokens()) {
 
            throw new UnauthorizedException("Aplicação não autorizada na HubSpot (faça OAuth)");
        }
        String email = contactProps.get("email").toString();


        return hubSpotClient.searchContactByEmail(email)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException("Contato com email '" + email + "' já existe"));
                    }

                    return hubSpotClient.createContact(contactProps);
                });
    }


    public Mono<Map<String, Object>> findById(String id) {
        if (!tokenManager.hasTokens()) {
            throw new UnauthorizedException("Aplicação não autorizada na HubSpot (faça OAuth)");
        }

        return hubSpotClient.searchContactById(id)
                .onErrorResume(ex -> {
                    if (ex instanceof WebClientResponseException.NotFound) {
                        return Mono.error(new NotFoundException("Contato com id '" + id + "' não encontrado"));
                    }
                    return Mono.error(ex);
                });
    }
}
