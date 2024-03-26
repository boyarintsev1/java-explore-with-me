package ru.practicum.ewm_main_service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс EventRequestStatusUpdateResult содержит результат подтверждения/отклонения заявок на участие в событии.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}

