package com.meetime.desafio.application.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meetime.desafio.service.ContactService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public Mono<ResponseEntity<String>> createContact(@RequestBody Map<String, Object> contactProps) {
        return contactService.createContact(contactProps)
                .map(contact -> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contact));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> findById(@PathVariable String id) {
        return contactService.findById(id)
                .map(contact -> ResponseEntity
                .status(HttpStatus.OK)
                .body(contact));
    }
}