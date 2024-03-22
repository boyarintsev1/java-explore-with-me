package ru.practicum.ewm_stats.server.service;

import ru.practicum.ewm_stats.dto.ViewStats;
import ru.practicum.ewm_stats.server.entity.EndpointHit;

import java.util.List;

/**
 * интерфейс для работы с данными статистики
 */
public interface StatsService {

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    EndpointHit createEndpointHit(EndpointHit endpointHit);

    /**
     * Метод получения статистики по посещениям.
     */
    List<ViewStats> findViewStats(String start, String end, String[] uris, Boolean uniqueIp);

    /**
     * метод определения уникальности IP-адреса для конкретного события (URI);
     */
    Boolean checkUniqueIpForUri(String ip, String uri);

}
