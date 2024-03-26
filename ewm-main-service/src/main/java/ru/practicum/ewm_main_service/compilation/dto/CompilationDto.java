package ru.practicum.ewm_main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;

import java.util.List;

/**
 * Класс CompilationDto содержит информацию о подборке событий, которая будет возвращена пользователю.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    /**
     * Список идентификаторов событий входящих в подборку.
     */
    private List<EventShortDto> events;

    /**
     * Идентификатор подборки.
     */
    private Long id;

    /**
     * Закреплена ли подборка на главной странице сайта?
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    private String title;
}



