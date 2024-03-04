package ru.practicum.ewm_stats.server.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_stats.dto.EndpointHitRequestDto;
import ru.practicum.ewm_stats.server.entity.EndpointHit;

/**
 * Класс EndpointHitMapper позволяет преобразовать данные EndpointHitRequestDto в EndpointHit.
 */
@Component
@RequiredArgsConstructor
public class EndpointHitMapper {
    public EndpointHit toEndpointHit(EndpointHitRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return EndpointHit
                .builder()
                .app(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .build();
    }
}
