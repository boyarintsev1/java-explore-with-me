package ru.practicum.ewm_main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.location.dto.LocationShortDto;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Класс EventFullDto содержит полное описание события, которое будет возвращено при запросах.
 * Дополнительные поля по сравнению с Event:
 * * Views содержит информацию о кол-ве просмотров события.
 */
@Getter
@Setter
@Builder
@JsonPropertyOrder({"annotation", "category", "confirmedRequests", "createdOn", "description", "eventDate", "id",
        "initiator", "location", "paid", "participantLimit", "publishedOn", "requestModeration", "state", "title",
        "views"})
@AllArgsConstructor
public class EventFullDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String annotation;
    @NotNull
    @NotEmpty
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
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
    private LocationShortDto location;
    @NotNull
    @NotEmpty
    private boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    @NotNull
    @NotBlank
    @NotEmpty
    private String title;
    private Long views;
}

