package ru.practicum.ewm_main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Класс UpdateCompilationRequest содержит информацию c входящего POST/PATCH запроса об изменении информации
 * о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    /**
     * Список id событий подборки для полной замены текущего списка.
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @Size(min = 1, max = 50, message = "Заголовок подборки должен быть длиной от 1 до 50 символов")
    private String title;
}
