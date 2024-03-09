package ru.practicum.ewm_stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * EndpointHitDto содержит описание EndpointHit, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@JsonPropertyOrder({"app", "uri", "ip", "timestamp"})
@AllArgsConstructor
public class EndpointHitDto {

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
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$",
            message = "Неверно указан IP-адрес. Проверьте его формат.")
    private String ip;

    @FutureOrPresent(message = "Дата посещения не может быть из будущего")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
