package ru.practicum.ewm_main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс EventRequestStatusUpdateRequest содержит информацию c входящего POST/PATCH запроса на изменение статуса
 * запроса на участие в событии текущего пользователя.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    /**
     * requestIds — идентификаторы запросов на участие в событии текущего пользователя;
     */
    private List<Long> requestIds;

    /**
     * status — новый статус запроса на участие в событии текущего пользователя;
     */
    private String status;

}
