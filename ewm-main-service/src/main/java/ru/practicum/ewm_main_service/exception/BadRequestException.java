package ru.practicum.ewm_main_service.exception;

public class BadRequestException extends RuntimeException {
    private final String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
