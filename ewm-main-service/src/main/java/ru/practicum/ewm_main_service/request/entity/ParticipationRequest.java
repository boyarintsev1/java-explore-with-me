package ru.practicum.ewm_main_service.request.entity;

import lombok.*;
import ru.practicum.ewm_main_service.enums.Status;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * Класс ParticipationRequest содержит информацию о запросах на участие в событии.
 */
@Entity
@Table(schema = "public", name = "P_Requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ParticipationRequest {

    /**
     * id — уникальный идентификатор заявки;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * created — дата и время создания заявки;
     */
    @Column(name = "created")
    @PastOrPresent(message = "Дата создания не может быть из будущего")
    private LocalDateTime created;

    /**
     * event — идентификатор события;
     */
    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;

    /**
     * requester — идентификатор пользователя, отправившего заявку.
     */
    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;

    /**
     * status — статус заявки.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}









