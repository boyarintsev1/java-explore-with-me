package ru.practicum.ewm_main_service.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm_main_service.category.entity.Category;
import ru.practicum.ewm_main_service.enums.State;
import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс Event содержит информацию о событиях.
 */
@Entity
@Table(schema = "public", name = "events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Event {

    /**
     * id — уникальный идентификатор события;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * annotation — краткое описание события;
     */
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    /**
     * category — категория, к которой относится событие;
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * confirmedRequests — количество одобренных заявок на участие в данном событии;
     */
    @Column(name = "confirmedRequests", nullable = false)
    private Long confirmedRequests;

    /**
     * createdOn — дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss");
     */
    @Column(name = "created_on", nullable = false)
    @PastOrPresent(message = "Дата создания не может быть из будущего")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * description — полное описание события;
     */
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    /**
     * eventDate — дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @Column(name = "event_date", nullable = false)
    @FutureOrPresent(message = "Дата создания не может быть из будущего")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * initiator — пользователь.
     */
    @ManyToOne
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;

    /**
     * Location — широта и долгота места проведения события.
     */
    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    private Location location;

    /**
     * paid — нужно ли оплачивать участие в событии.
     */
    @Column(name = "paid", nullable = false)
    private Boolean paid;

    /**
     * participantLimit — ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
     */
    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;

    /**
     * publishedOn — дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss");
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    /**
     * requestModeration — нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать
     * подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     */
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    /**
     * state — список состояний жизненного цикла события.
     */
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    /**
     * title — заголовок события;
     */
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    /**
     * views содержит информацию о кол-ве просмотров события.
     */
    @Column(name = "views", nullable = false)
    private Long views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(getAnnotation(), event.getAnnotation()) && Objects.equals(getCategory(),
                event.getCategory()) && Objects.equals(getCreatedOn(), event.getCreatedOn())
                && Objects.equals(getDescription(), event.getDescription()) && Objects.equals(getEventDate(),
                event.getEventDate()) && Objects.equals(getInitiator(), event.getInitiator())
                && Objects.equals(getLocation(), event.getLocation()) && getState() == event.getState()
                && Objects.equals(getTitle(), event.getTitle()) && Objects.equals(getViews(), event.getViews());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventDate(), getInitiator(), getLocation(), getState());
    }
}
