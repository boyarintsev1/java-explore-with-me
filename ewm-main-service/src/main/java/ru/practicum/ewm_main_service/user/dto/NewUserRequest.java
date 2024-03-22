package ru.practicum.ewm_main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс NewUserRequest содержит информацию c входящего POST/PATCH запроса о пользователях (user).
 */
@Getter
@AllArgsConstructor
public class NewUserRequest {

    @NotNull(message = "Значение поля Email не может быть пустым")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Size(min = 6, max = 254, message
            = "Адрес электронной почты должен быть длиной от 6 до 254 символов")
    private String email;

    @NotNull(message = "Значение поля Name не может быть пустым")
    @NotBlank
    @Size(min = 2, max = 250, message
            = "Имя пользователя должно быть длиной от 2 до 250 символов")
    private String name;
}

