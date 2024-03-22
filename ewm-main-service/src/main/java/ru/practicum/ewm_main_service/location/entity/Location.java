package ru.practicum.ewm_main_service.location.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;

/**
 * Класс Location содержит информацию о широте и долготе указанного места.
 */
@Entity
@Table(schema = "public", name = "locations",
        uniqueConstraints = { @UniqueConstraint(name = "UQ_LOCATION", columnNames = { "lat", "lon" })})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "lat", "lon"})
public class Location {

     /**
     * id — уникальный идентификатор локации;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @JsonIgnore
    private Long id;

    /**
     * lat - широта указанного места.
     */
    @Column(name = "lat", nullable = false)
    private float lat;

    /**
     * lon - долгота указанного места.
     */
    @Column(name = "lon", nullable = false)
    private float lon;
}
