package ru.practicum.ewm_main_service.request.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 * Класс ParticipationRequestDto содержит описание заявки на участие событии, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"created", "event", "id", "requester", "status"})
public class ParticipationRequestDto {

    /**
     * created — дата и время создания заявки;
     */
    private String created;

    /**
     * event — идентификатор события;
     */
    private Long event;

    /**
     * id — идентификатор заявки;
     */
    private Long id;

    /**
     * requester — идентификатор пользователя, отправившего заявку.
     */
    private Long requester;

    /**
     * status — статус заявки.
     */
    private String status;

}








