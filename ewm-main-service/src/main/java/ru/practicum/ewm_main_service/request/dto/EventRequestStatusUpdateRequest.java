package ru.practicum.ewm_main_service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс EventRequestStatusUpdateRequest содержит запрос на изменение статуса запроса на участие в событии
 * текущего пользователя
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    /**
     * requestIds — идентификаторы запросов на участие в событии текущего пользователя.
     */
    private List<Long> requestIds;

    /**
     * status — новый статус запроса на участие в событии текущего пользователя.
     */
    private String status;
}
