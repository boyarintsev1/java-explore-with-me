package ru.practicum.ewm_stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_stats.dto.EndpointHitRequestDto;
import ru.practicum.ewm_stats.dto.ViewStats;
import ru.practicum.ewm_stats.server.entity.EndpointHit;
import ru.practicum.ewm_stats.server.mapper.EndpointHitMapper;
import ru.practicum.ewm_stats.server.service.StatsService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер для работы со статистикой посещений сервисов
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private final EndpointHitMapper endpointHitMapper;

    /**
     * Метод сохранения информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    @PostMapping(path = "/hit")
    public ResponseEntity<EndpointHit> createEndpointHit(@Valid @RequestBody EndpointHitRequestDto endpointHitRequestDto) {
        EndpointHit endpointHit = endpointHitMapper.toEndpointHit(endpointHitRequestDto);
        log.info("Информация сохранена");
        return new ResponseEntity<>(statsService.createEndpointHit(endpointHit), HttpStatus.CREATED);
    }

    /**
     * Метод получения статистики по посещениям.
     */
    @GetMapping(path = "/stats")
    public ResponseEntity<List<ViewStats>> findViewStats(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean uniqueIp) {
        log.info("Статистика собрана");
        return new ResponseEntity<>(statsService.findViewStats(start, end, uris, uniqueIp), HttpStatus.OK);
    }
}
