package ru.practicum.ewm_main_service.request.service;

import ru.practicum.ewm_main_service.enums.Status;
import ru.practicum.ewm_main_service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_main_service.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;

import java.util.List;

/**
 * интерфейс для работы с данными о запросах Request на участие в событиях
 */
public interface RequestService {

    /**
     * метод получения информации о количестве запросов на событие Event с определенным статусом Status
     */
    Long countRequestsByEventIdAndStatus(Long eventId, Status status);

    /**
     * метод получения информации о заявках текущего пользователя на участие в чужих событиях
     */
    List<ParticipationRequest> findRequestsByUserId(Long userId);

    /**
     * метод получения информации о запросах на участие в событии текущего пользователя
     */
    List<ParticipationRequest> findRequestsToEventOfCurrentUser(Long userId, Long eventId);

    /**
     * метод создания нового запроса
     */
    ParticipationRequest createRequest(Long userId, Long eventId);

    /**
     * отмена запроса текущего пользователя на участие в событии
     */
    ParticipationRequest cancelRequestByRequester(Long userId, Long requestId);

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId,
                                                            EventRequestStatusUpdateRequest request);
}
