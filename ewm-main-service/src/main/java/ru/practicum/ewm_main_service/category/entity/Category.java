package ru.practicum.ewm_main_service.category.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;

/**
 * Класс Category содержит информацию о категориях.
 */
@Entity
@Table(schema = "public", name = "categories",
        uniqueConstraints = @UniqueConstraint(name = "UQ_CATEGORY_NAME", columnNames = {"name"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class Category {

    /**
     * id — уникальный идентификатор категории;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * name — название категории;
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;
}


