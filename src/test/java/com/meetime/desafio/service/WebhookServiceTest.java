package com.meetime.desafio.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.meetime.desafio.core.dto.HubSpotProperties;

class WebhookServiceTest {
    private WebhookService webhookService;
    private String secret = "myClientSecret";
    
    @BeforeEach
    void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        HubSpotProperties mockProperties = mock(HubSpotProperties.class);
        when(mockProperties.getClientSecret()).thenReturn(secret);
        webhookService = new WebhookService(mockProperties);
        Field secretField = WebhookService.class.getDeclaredField("clientSecret");
        secretField.setAccessible(true);
        secretField.set(webhookService, secret);
    }
    
    @Test
    void requestIsValid_ShouldReturnTrueForValidSignature() throws InvalidKeyException, NoSuchAlgorithmException {
        String requestBody = "{\"test\": \"data\"}";
        String expectedSignature = calculateHmacSHA256(requestBody, secret);
        
        boolean result = webhookService.requestIsValid(requestBody, expectedSignature);
        assertTrue(result, "A assinatura válida deveria retornar true");
    }
    
    @Test
    void requestIsValid_ShouldReturnFalseForInvalidSignature() {
        String requestBody = "{\"test\": \"data\"}";
        String wrongSignature = "abcd1234";
        
        boolean result = webhookService.requestIsValid(requestBody, wrongSignature);
        assertFalse(result, "Uma assinatura inválida deveria retornar false");
    }

    private String calculateHmacSHA256(String data, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }
}

