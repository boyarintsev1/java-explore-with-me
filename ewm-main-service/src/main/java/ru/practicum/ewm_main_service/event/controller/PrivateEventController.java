package ru.practicum.ewm_main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm_main_service.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по Event (приватный)
 */
@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper participationRequestMapper;

    /**
     * метод получения событий, добавленных текущим пользователем
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<EventShortDto>> findEventsOfCurrentUser(
            @PathVariable("userId") @Positive Long userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("События найдены");
        return eventService.findAllEventsOfCurrentUser(userId, from, size).isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(eventService.findAllEventsOfCurrentUser(userId, from, size)
                .map(eventMapper::toEventShortDto)
                .getContent(), HttpStatus.OK);
    }

    /**
     * метод получения полной информации о событии, добавленном текущим пользователем.
     */
    @GetMapping(path = "/{eventId}", headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> findEventFullDtoOfCurrentUser(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("eventId") @Positive Long eventId) {
        log.info("Событие найдены");
        return new ResponseEntity<>(eventMapper.toEventFullDto(
                eventService.findEventOfCurrentUserById(userId, eventId)), HttpStatus.OK);
    }

    /**
     * метод создания нового события
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") @Positive Long userId,
                                                    @Valid @RequestBody NewEventDto newEventDto) {
        Event event = eventMapper.toEvent(newEventDto, userId);
        log.info("Событие добавлено.");
        return new ResponseEntity<>(eventMapper.toEventFullDto(eventService.createEvent(event)),
                HttpStatus.CREATED);
    }

    /**
     * Изменение события, добавленного текущим пользователем
     */
    @PatchMapping(path = "/{eventId}", headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> updateEventByUser(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("eventId") @Positive Long eventId,
            @Valid @NotNull @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Событие отредактировано.");
        return new ResponseEntity<>(eventMapper.toEventFullDto(eventService.updateEventByUser(userId, eventId,
                updateEventUserRequest)),
                HttpStatus.OK);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping(path = "/{eventId}/requests", headers = "Accept=application/json")
    public ResponseEntity<List<ParticipationRequestDto>> findRequestsToEventOfCurrentUser(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("eventId") @Positive Long eventId) {
        log.info("Найдены запросы на участие");
        return requestService.findRequestsToEventOfCurrentUser(userId, eventId).isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(requestService.findRequestsToEventOfCurrentUser(userId, eventId)
                .stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    @PatchMapping(path = "/{eventId}/requests", headers = "Accept=application/json")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequestStatus(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("eventId") @Positive Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Статус заявок изменён");
        return new ResponseEntity<>(requestService.updateEventRequestStatus(userId, eventId, request),
                HttpStatus.OK);
    }
}

