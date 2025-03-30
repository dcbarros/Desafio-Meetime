package com.meetime.desafio.config.exceptions;

public class TooManyRequestsException extends AbstractException {
    public TooManyRequestsException(String message) { super(message); }
    @Override
    public org.springframework.http.HttpStatus getStatus() { return org.springframework.http.HttpStatus.TOO_MANY_REQUESTS; }
}
