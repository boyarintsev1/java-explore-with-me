package ru.practicum.ewm_stats_client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm_stats_client.client.ViewStatsClient;

/**
 * Класс-контроллер для работы со статистикой посещений сервисов ViewStats
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stats")
public class ViewStatsController {

    private final ViewStatsClient viewStatsClient;

    /**
     * метод валидации входящих данных перед получением статистики по посещениям.
     */
    @GetMapping
    public ResponseEntity<Object> findViewStats(
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean uniqueIp) {
        return viewStatsClient.findViewStats(start, end, uris, uniqueIp);
    }
}
