package ru.practicum.ewm_stats.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * EndpointHitRequestDto содержит информацию c входящего POST запроса о посещении сервиса.
 */
@Getter
@Builder
@JsonPropertyOrder({"app", "uri", "ip"})
@AllArgsConstructor
public class EndpointHitRequestDto {

    @NotNull(message = "Поле APP отсутствует")
    @NotBlank(message = "Поле APP не может быть пустым")
    @Size(min = 1, max = 30, message = "Название сервиса, для которого записывается информация, " +
            "должно быть длиной от 1 до 20 символов")
    private String app;

    @NotNull(message = "Поле URI отсутствует")
    @NotBlank(message = "Поле URI не может быть пустым")
    @Size(min = 1, max = 30, message = "URI, для которого был осуществлен запрос, " +
            "должен быть длиной от 1 до 30 символов")
    private String uri;

    @NotNull(message = "Поле IP отсутствует")
    private String ip;
}
