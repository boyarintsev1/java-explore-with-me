package ru.practicum.ewm_main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Класс NewCompilationDto содержит информацию c входящего POST/PATCH запроса о подборке событий.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    /**
     * Список идентификаторов событий входящих в подборку.
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @NotBlank(message = "Значение поля Title не может быть пустым")
    @Size(min = 1, max = 50, message = "Заголовок подборки должен быть длиной от 1 до 50 символов")
    private String title;
}



