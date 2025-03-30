package com.meetime.desafio.infrastructure.hubspot;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.meetime.desafio.core.dto.HubSpotProperties;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HubSpotClient {

    private final WebClient webClient;
    private final HubSpotProperties hubSpotProperties;

    public Mono<Boolean> searchContactByEmail(String Email) {
        String searchUrl = hubSpotProperties.getApiUrl() + "/crm/v3/objects/contacts/search";

        Map<String, Object> filterRequest = Map.of(
            "filterGroups", new Object[] {
                Map.of("filters", new Object[] {
                    Map.of(
                        "propertyName", "email",
                        "operator", "EQ",
                        "value", Email
                    )
                })
            },
            "properties", new String[] { "email" },
            "limit", 1
        );

        return webClient.post()
                .uri(searchUrl)
                .bodyValue(filterRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {

                    var results = (java.util.List<?>) response.get("results");
                    return results != null && !results.isEmpty();
                });
    }

    public Mono<Map<String, Object>> searchContactById(String id) {
        String url = hubSpotProperties.getApiUrl() + "/crm/v3/objects/contacts/" + id;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<String> createContact(Map<String, Object> contactProperties) {
        String createUrl = hubSpotProperties.getApiUrl() + "/crm/v3/objects/contacts";

        Map<String, Object> requestBody = Map.of("properties", contactProperties);
        return webClient.post()
                .uri(createUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> response.get("id").toString()); 
    }
}
