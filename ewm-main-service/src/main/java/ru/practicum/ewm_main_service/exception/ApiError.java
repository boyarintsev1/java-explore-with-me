package ru.practicum.ewm_main_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"errors", "status", "reason", "message", "timestamp"})
public class ApiError {
    private final List<String> errors = new ArrayList<>();
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;
}
