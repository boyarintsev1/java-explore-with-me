package ru.practicum.ewm_main_service.user.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;

/**
 * Класс User содержит информацию о пользователях (user).
 */
@Entity
@Table(schema = "public", name = "users",
        uniqueConstraints = @UniqueConstraint(name = "UQ_USER_EMAIL", columnNames = {"email"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"id", "name", "email"})
public class User {

    /**
     * id — уникальный идентификатор пользователя;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * name — имя пользователя;
     */
    @Column(name = "name", nullable = false, length = 250)
    private String name;

    /**
     * email — адрес электронной почты пользователя.
     */
    @Column(name = "email", nullable = false, length = 254)
    private String email;

}





