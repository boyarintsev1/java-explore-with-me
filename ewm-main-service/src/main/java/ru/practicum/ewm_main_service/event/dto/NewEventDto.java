package ru.practicum.ewm_main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_main_service.location.entity.Location;

import javax.validation.constraints.*;

/**
 * Класс NewEventDto содержит информацию c входящего POST/PATCH запроса для добавления/обновления события Event.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    /**
     * annotation — краткое описание события;
     */
    @NotBlank(message = "Значение поля Annotation не может быть пустым")
    @Size(min = 20, max = 2000, message = "Описание события должно быть длиной от 20 до 2000 символов")
    private String annotation;

    /**
     * category — ID категории к которой относится событие;
     */
    @NotNull(message = "Значение поля Category не может быть пустым")
    private Long category;

    /**
     * description — полное описание события;
     */
    @NotBlank(message = "Значение поля Description не может быть пустым")
    @Size(min = 20, max = 7000, message = "Полное описание события должно быть длиной от 20 до 7000 символов")
    private String description;

    /**
     * eventDate — дата и время на которые намечено событие.
     */
    @NotNull(message = "Значение поля EventDate не может быть пустым")
    @NotEmpty(message = "Значение поля EventDate не может быть пустым")
    @Pattern(regexp =
            "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|" +
                    "(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|" +
                    "[13579][26])00)-02-29) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9]$",
            message = "Неверно указан формат. Дата и время указываются в формате yyyy-MM-dd HH:mm:ss")
    private String eventDate;

    /**
     * Location — широта и долгота места проведения события.
     */
    @NotNull(message = "Значение поля Location не может быть пустым")
    private Location location;

    /**
     * paid — нужно ли оплачивать участие в событии.
     */
    private Boolean paid;

    /**
     * participantLimit — ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    @PositiveOrZero
    private Long participantLimit;

    /**
     * requestModeration — нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать
     * подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

    /**
     * title — заголовок события;
     */
    @NotBlank(message = "Значение поля Title не может быть пустым")
    @Size(min = 3, max = 120, message = "Заголовок события должен быть длиной от 3 до 120 символов")
    private String title;
}
