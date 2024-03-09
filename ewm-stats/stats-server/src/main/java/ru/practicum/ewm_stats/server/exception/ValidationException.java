package ru.practicum.ewm_stats.server.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private final String parameter;
    private final HttpStatus status;

    public ValidationException(String parameter, HttpStatus status) {
        this.parameter = parameter;
        this.status = status;
    }

    public String getParameter() {
        return parameter;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
