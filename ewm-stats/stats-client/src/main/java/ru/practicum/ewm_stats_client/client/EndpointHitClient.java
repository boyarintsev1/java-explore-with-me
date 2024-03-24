package ru.practicum.ewm_stats_client.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm_stats.dto.EndpointHitRequestDto;
import ru.practicum.ewm_stats_client.common.client.BaseClient;

import java.time.Duration;

/**
 * Класс-клиент по посещениям EndpointHit
 */
@Service
public class EndpointHitClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

  /*  HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
 requestFactory.setConnectTimeout(10*1000);
 requestFactory.
    RestTemplate rest = new RestTemplate(requestFactory); */

    @Autowired
    public EndpointHitClient(@Value("${STATS_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .setConnectTimeout(Duration.ofMillis(20000))
                        .setReadTimeout(Duration.ofMillis(20000))
                        .build()
        );
    }




    public ResponseEntity<Object> createEndpointHit(EndpointHitRequestDto endpointHitRequestDto) {
        return post("", null, endpointHitRequestDto);
    }
}
