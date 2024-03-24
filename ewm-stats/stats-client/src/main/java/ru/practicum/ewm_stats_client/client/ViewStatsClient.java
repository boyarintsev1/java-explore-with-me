package ru.practicum.ewm_stats_client.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm_stats_client.common.client.BaseClient;

import java.util.Map;

/**
 * Класс-клиент по статистике посещений ViewStats
 */
@Service
public class ViewStatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";  //

    @Autowired
    public ViewStatsClient(@Value("${STATS_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findViewStats(String start, String end, String[] uris, Boolean uniqueIp) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", uniqueIp
        );
        return get("?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> checkUniqueIpForUri(String ip, String uri) {
        Map<String, Object> parameters = Map.of(
                "ip", ip,
                "uri", uri
        );
        return get("/{ip}?uri={uri}", parameters);
    }
}