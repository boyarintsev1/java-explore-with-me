package ru.practicum.ewm_stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_stats.dto.ViewStats;
import ru.practicum.ewm_stats.server.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.ewm_stats.dto.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "from EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStats> findViewStatsWithUriAndIp(LocalDateTime start, LocalDateTime end, String[] uris, Boolean uniqueIp);

    @Query("SELECT new ru.practicum.ewm_stats.dto.ViewStats(eh.app, eh.uri, COUNT(eh.id)) " +
            "from EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.id) DESC")
    List<ViewStats> findViewStatsWithUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.ewm_stats.dto.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "from EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStats> findViewStatsWithIp(LocalDateTime start, LocalDateTime end, Boolean uniqueIp);

    @Query("SELECT new ru.practicum.ewm_stats.dto.ViewStats(eh.app, eh.uri, COUNT(eh.id)) " +
            "from EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.id) DESC")
    List<ViewStats> findViewStatsWithoutUriAndIp(LocalDateTime start, LocalDateTime end);
}