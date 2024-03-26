package ru.practicum.ewm_main_service.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс UserShortDto для краткого отображения информации о User.
 */
@Getter
@Setter
@Builder
public class UserShortDto {
    private Long id;
    private String name;

    public UserShortDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}