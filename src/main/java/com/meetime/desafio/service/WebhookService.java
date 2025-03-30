package com.meetime.desafio.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {
    @Value("${hubspot.client-secret}")
    private String clientSecret;
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    public boolean requestIsValid(String requestBody, String signature) {
        try {
            String computedSignature = computeSignature(requestBody);
            return computedSignature.equals(signature);
        } catch (Exception e) {
            logger.error("Erro ao validar assinatura do webhook", e);
            return false;
        }
    }

    private String computeSignature(String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec key = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] hash = mac.doFinal(requestBody.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }
}

