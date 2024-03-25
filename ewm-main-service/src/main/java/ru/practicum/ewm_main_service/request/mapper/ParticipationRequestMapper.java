package ru.practicum.ewm_main_service.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;

import java.time.format.DateTimeFormatter;

/**
 * Класс ParticipationRequestMapper позволяет преобразовать сущность ParticipationRequest в формат исходящих данных
 * ParticipationRequestDto.
 */
@UtilityClass
public class ParticipationRequestMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        if (participationRequest == null) {
            return null;
        }
        return ParticipationRequestDto
                .builder()
                .created(formatter.format(participationRequest.getCreated()))
                .event(participationRequest.getEvent().getId())
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().toString())
                .build();
    }
}


