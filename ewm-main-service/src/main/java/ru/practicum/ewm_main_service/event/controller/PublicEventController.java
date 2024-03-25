package ru.practicum.ewm_main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по Event (публичный)
 */
@RestController
@Slf4j
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * метод получения событий с возможностью фильтрации:
     * text - текст для поиска в содержимом аннотации и подробном описании события;
     * categories - список идентификаторов категорий в которых будет вестись поиск;
     * paid - поиск только платных/бесплатных событий;
     * rangeStart - дата и время не раньше которых должно произойти событие;
     * rangeEnd - дата и время не позже которых должно произойти событие;
     * onlyAvailable - только события, у которых не исчерпан лимит запросов на участие;
     * sort - Вариант сортировки: по дате события или по количеству просмотров;
     * from - количество событий, которые нужно пропустить для формирования текущего набора;
     * size - количество событий в наборе.
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<EventShortDto>> findEvents(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) Integer[] categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("Выполняется запрос на поиск событий...");
        Page<Event> foundedEvents = eventService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
        return new ResponseEntity<>(foundedEvents.getContent().stream()
                .map(eventMapper::toEventShortDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    /**
     * получение подробной информации об опубликованном событии по его идентификатору
     */
    @GetMapping(path = "/{id}", headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> findEventById(@PathVariable("id") Long eventId, HttpServletRequest request) {
        log.info("Выполняется запрос на поиск события по его ID...");
        Event foundedEvent = eventService.findEventById(eventId, request);
        return new ResponseEntity<>(eventMapper.toEventFullDto(foundedEvent), HttpStatus.OK);
    }
}

