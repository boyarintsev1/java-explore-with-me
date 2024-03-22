package ru.practicum.ewm_main_service.category.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс CategoryDto содержит описание категории, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@JsonPropertyOrder({"id", "name"})
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
}

