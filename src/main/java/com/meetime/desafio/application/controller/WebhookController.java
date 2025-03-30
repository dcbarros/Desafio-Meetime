package com.meetime.desafio.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meetime.desafio.service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestBody String requestBody,
            @RequestHeader(name = "X-HubSpot-Signature", required = false) String signature,
            @RequestHeader(name = "X-HubSpot-Signature-Version", required = false) String signatureVersion) {

        if (signature == null || signature.isEmpty()) {
            return ResponseEntity.status(400).body("Missing X-HubSpot-Signature header");
        }

        boolean valid = webhookService.requestIsValid(requestBody, signature);
        if (!valid) {
            return ResponseEntity.status(403).body("Invalid signature");
        }

        System.out.println("Webhook recebido: " + requestBody);
        return ResponseEntity.ok("Webhook processed");
    }
}
