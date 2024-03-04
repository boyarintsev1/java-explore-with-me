package ru.practicum.ewm_stats.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс EndpointHit ("отзыв") содержит информацию (записи) об обращении к сервисам.
 */
@Entity
@Table(schema = "public", name = "stats")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "app", "uri", "ip", "timestamp"})
public class EndpointHit {

        /**
         * id — уникальный идентификатор записи;
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, updatable = false)
        private Long id;

        /**
         * app — название сервиса для которого записывается информация;
         */
        @Column(name = "app", nullable = false, length = 30)
        private String app;

        /**
         * uri — URI, для которого был осуществлен запрос;
         */
        @Column(name = "uri", nullable = false, length = 30)
        private String uri;

        /**
         * ip — IP-адрес пользователя, осуществившего запрос;
         */
        @Column(name = "ip", nullable = false, length = 39)
        private String ip;

        /**
         * timestamp — дата и время, когда был совершен запрос к эндпойнту;
         */
        @Column(name = "created", nullable = false, unique = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;
}
