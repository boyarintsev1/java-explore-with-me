package ru.practicum.ewm_stats_client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_stats.dto.EndpointHitRequestDto;
import ru.practicum.ewm_stats_client.client.EndpointHitClient;

import javax.validation.Valid;

/**
 * Класс-контроллер для работы с посещениями EndpointHit
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/hit")
public class EndpointHitController {

    private final EndpointHitClient endpointHitClient;

    /**
     * метод валидации входящих данных перед созданием нового запроса пользователя
     */
    @PostMapping
    public ResponseEntity<Object> createEndpointHit(@Valid @RequestBody EndpointHitRequestDto endpointHitRequestDto) {
        return endpointHitClient.createEndpointHit(endpointHitRequestDto);
    }
}
