package com.meetime.desafio.config.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractException {

    public ConflictException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
