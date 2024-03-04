package ru.practicum.ewm_stats.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * ViewStats содержит информацию об истории посещений сервисов.
 */
@Getter
@Builder
@JsonPropertyOrder({"app", "uri", "hits"})
@AllArgsConstructor
public class ViewStats {
    /**
     * app — название сервиса, для которого записывается информация;
     */
    private String app;

    /**
     * uri — URI сервиса;
     */
    private String uri;

    /**
     * hits — количество просмотров;
     */
    private Long hits;
}
