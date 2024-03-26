package ru.practicum.ewm_main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Класс EventRequestStatusUpdateResult содержит информацию о подтверждения/отклонения заявок на участие в событии
 * текущего пользователя, которая будет возвращена пользователю.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    /**
     * confirmedRequests — подтвержденные заявки;
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /**
     * rejectedRequests — отклоненные заявки;
     */
    private List<ParticipationRequestDto> rejectedRequests;

}
