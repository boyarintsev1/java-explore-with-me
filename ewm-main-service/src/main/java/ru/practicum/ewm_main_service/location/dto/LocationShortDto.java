package ru.practicum.ewm_main_service.location.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс LocationShortDto содержит краткое описание локации, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"lat", "lon"})
public class LocationShortDto {
    private Float lat;
    private Float lon;

}
