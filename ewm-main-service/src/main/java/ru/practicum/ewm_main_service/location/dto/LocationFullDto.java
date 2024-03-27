package ru.practicum.ewm_main_service.location.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс LocationDto содержит полное описание локации, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "lat", "lon"})
public class LocationFullDto {
    private Long id;
    private Float lat;
    private Float lon;
    private String description;
    private String title;
}
