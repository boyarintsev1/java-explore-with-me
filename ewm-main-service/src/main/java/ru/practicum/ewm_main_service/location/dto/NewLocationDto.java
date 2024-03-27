package ru.practicum.ewm_main_service.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * Класс LocationDto содержит описание локации, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewLocationDto {

    /**
     * title — заголовок локации;
     */
    @Size(min = 3, max = 120, message = "Заголовок локации должен быть длиной от 3 до 120 символов")
    private String title;

    /**
     * description — описание локации;
     */
    @Size(min = 20, max = 7000, message = "Полное описание события должно быть длиной от 20 до 7000 символов")
    private String description;

    /**
     * lat - широта указанного места.
     */
    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;

    /**
     * lon - долгота указанного места.
     */
    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;

}
