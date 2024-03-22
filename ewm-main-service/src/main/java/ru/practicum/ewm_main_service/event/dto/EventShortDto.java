package ru.practicum.ewm_main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Класс EventShortDto содержит краткое описание события, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventShortDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String annotation;
    @NotNull
    @NotEmpty
    private CategoryDto category;
    private Long confirmedRequests;
    @NotNull
    @NotBlank
    @NotEmpty
    private String eventDate;
    private Long id;
    @NotNull
    @NotEmpty
    private UserShortDto initiator;
    @NotNull
    @NotEmpty
    private boolean paid;
    @NotNull
    @NotBlank
    @NotEmpty
    private String title;
    private Long views;
}
