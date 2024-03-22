package ru.practicum.ewm_main_service.compilation.entity;

import lombok.*;
import ru.practicum.ewm_main_service.event.entity.Event;

import javax.persistence.*;
import java.util.List;

/**
 * Класс Event содержит информацию о событиях.
 */
@Entity
@Table(schema = "public", name = "compilations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Compilation {

    /**
     * Идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;


    /**
     * Список событий входящих в подборку.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @Column(name = "title", nullable = false, length = 50, unique = true)
    private String title;

}
