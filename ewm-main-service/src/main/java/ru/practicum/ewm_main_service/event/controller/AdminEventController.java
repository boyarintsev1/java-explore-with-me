package ru.practicum.ewm_main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по Event (админ)
 */
@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * метод получения событий с возможностью фильтрации:
     * users - список id пользователей, чьи события нужно найти;
     * states - список состояний в которых находятся искомые события;
     * categories - список идентификаторов категорий в которых будет вестись поиск;
     * rangeStart - дата и время не раньше которых должно произойти событие;
     * rangeEnd - дата и время не позже которых должно произойти событие;
     * from - количество событий, которые нужно пропустить для формирования текущего набора;
     * size - количество событий в наборе.
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<EventFullDto>> findEvents(
            @RequestParam(value = "users", required = false) Long[] users,
            @RequestParam(value = "states", required = false) String[] states,
            @RequestParam(value = "categories", required = false) Integer[] categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("События найдены");
        return new ResponseEntity<>(eventService.findEvents(users, states, categories, rangeStart, rangeEnd, from,
                        size, request).getContent().stream()
                .map(eventMapper::toEventFullDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    /**
     * редактирование данных события и его статуса (отклонение/публикация) админом.
     */
    @PatchMapping(path = "/{eventId}", headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable("eventId") @Positive Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Событие отредактировано.");
        return new ResponseEntity<>(eventMapper.toEventFullDto(eventService.updateEventByAdmin(eventId,
                updateEventAdminRequest)),
                HttpStatus.OK);
    }
}

