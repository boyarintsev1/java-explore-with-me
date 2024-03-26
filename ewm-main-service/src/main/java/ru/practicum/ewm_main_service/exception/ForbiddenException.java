package ru.practicum.ewm_main_service.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public ForbiddenException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
