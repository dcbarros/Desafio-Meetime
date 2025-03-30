package com.meetime.desafio.config.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractException {
    public UnauthorizedException(String message) { super(message); }
    @Override
    public HttpStatus getStatus() { return HttpStatus.UNAUTHORIZED; }
}
