package com.meetime.desafio.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.meetime.desafio.service.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestBody JsonNode requestBody) {
        System.out.println("Webhook recebido: " + requestBody);
        return ResponseEntity.ok("Webhook processed");
    }
}
