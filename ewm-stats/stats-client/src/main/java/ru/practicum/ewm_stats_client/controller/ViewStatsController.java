package ru.practicum.ewm_stats_client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean uniqueIp) {
        return viewStatsClient.findViewStats(start, end, uris, uniqueIp);
    }

    /**
     * метод определения уникальности IP-адреса для конкретного события (URI);
     */
    @GetMapping("/{ip}")
    public ResponseEntity<Object> checkUniqueIpForUri(@PathVariable String ip,
                                                      @RequestParam(name = "uri") String uri) {
        return viewStatsClient.checkUniqueIpForUri(ip, uri);
    }
}
