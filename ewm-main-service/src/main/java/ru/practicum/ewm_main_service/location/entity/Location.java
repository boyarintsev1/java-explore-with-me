package ru.practicum.ewm_main_service.location.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
@JsonPropertyOrder({"id", "title", "description", "lat", "lon"})
public class Location {

    /**
     * id — уникальный идентификатор локации;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * title — заголовок локации;
     */
    @Column(name = "title", length = 120)
    private String title;

    /**
     * description — описание локации;
     */
    @Column(name = "description", length = 7000)
    private String description;

    /**
     * lat - широта указанного места.
     */
    @Column(name = "lat", nullable = false)
    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;

    /**
     * lon - долгота указанного места.
     */
    @Column(name = "lon", nullable = false)
    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(getLat(), location.getLat()) && Objects.equals(getLon(), location.getLon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLat(), getLon());
    }
}
