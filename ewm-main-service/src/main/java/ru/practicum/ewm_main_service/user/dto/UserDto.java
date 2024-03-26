package ru.practicum.ewm_main_service.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс UserDto ("пользователь") содержит описание User, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@JsonPropertyOrder({"email", "id", "name"})
@AllArgsConstructor
public class UserDto {
    private String email;
    private final Long id;
    private String name;
}

