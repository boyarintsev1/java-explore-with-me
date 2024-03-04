package ru.practicum.ewm_stats.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_stats.dto.ViewStats;
import ru.practicum.ewm_stats.server.entity.EndpointHit;
import ru.practicum.ewm_stats.server.exception.ValidationException;
import ru.practicum.ewm_stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * класс для работы с данными статистики при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public EndpointHit createEndpointHit(EndpointHit endpointHit) {
        endpointHit.setTimestamp(LocalDateTime.now());
        log.info("Создан объект: {}", endpointHit);
        return statsRepository.save(endpointHit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findViewStats(String start, String end, String[] uris, Boolean uniqueIp) {
        LocalDateTime startPeriod = LocalDateTime.parse(start, dtf);
        LocalDateTime endPeriod = LocalDateTime.parse(end, dtf);

        if (startPeriod.isAfter(endPeriod) || startPeriod.equals(endPeriod)) {
            throw new ValidationException("Дата начала периода не может быть после его окончания.",
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Исполняется запрос на получение статистики посещений.");

        if (uris != null) {
            if (uniqueIp) {
                return statsRepository.findViewStatsWithUriAndIp(startPeriod, endPeriod, uris, uniqueIp);
            }
            return statsRepository.findViewStatsWithUris(startPeriod, endPeriod, uris);
        } else {
            if (uniqueIp) {
                return statsRepository.findViewStatsWithIp(startPeriod, endPeriod, uniqueIp);
            }
            return statsRepository.findViewStatsWithoutUriAndIp(startPeriod, endPeriod);
        }
    }
}

