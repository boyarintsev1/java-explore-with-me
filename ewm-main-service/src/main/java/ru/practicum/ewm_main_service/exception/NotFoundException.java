package ru.practicum.ewm_main_service.exception;

public class NotFoundException extends RuntimeException {
    private final String message;
    private final Long id;
    private final String entityName;

    public NotFoundException(String message, Long id, String entityName) {
        this.message = message;
        this.id = id;
        this.entityName = entityName;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }
}
