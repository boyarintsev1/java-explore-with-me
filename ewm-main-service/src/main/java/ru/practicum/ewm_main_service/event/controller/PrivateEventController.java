package ru.practicum.ewm_main_service.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.*;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;
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

    /**
     * метод получения событий, добавленных текущим пользователем
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<EventShortDto>> findEventsOfCurrentUser(
            @PathVariable("userId") @Positive Long userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Выполняется запрос поиск событий...");
        Page<Event> foundedEvents = eventService.findAllEventsOfCurrentUser(userId, from, size);
        return foundedEvents.isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(foundedEvents
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
        log.info("Выполняется запрос на поиск события пользователем...");
        Event foundedEvent = eventService.findEventOfCurrentUserById(userId, eventId);
        return new ResponseEntity<>(eventMapper.toEventFullDto(foundedEvent), HttpStatus.OK);
    }

    /**
     * метод создания нового события
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") @Positive Long userId,
                                                    @Valid @RequestBody NewEventDto newEventDto) {
        Event event = eventMapper.toEvent(newEventDto, userId);
        log.info("Выполняется запрос на добавление нового события...");
        Event newEvent = eventService.createEvent(event);
        return new ResponseEntity<>(eventMapper.toEventFullDto(newEvent),
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
        log.info("Выполняется запрос на обновление события пользователем...");
        Event updatedEvent = eventService.updateEventByUser(userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(eventMapper.toEventFullDto(updatedEvent), HttpStatus.OK);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping(path = "/{eventId}/requests", headers = "Accept=application/json")
    public ResponseEntity<List<ParticipationRequestDto>> findRequestsToEventOfCurrentUser(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("eventId") @Positive Long eventId) {
        log.info("Выполняется запрос на поиск запросов на участие в событии пользователя...");
        List<ParticipationRequest> foundedRequests = requestService.findRequestsToEventOfCurrentUser(userId, eventId);
        return foundedRequests.isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(foundedRequests
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
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
        log.info("Выполняется запрос на изменение статуса запросов на участие в событии пользователя...");
        return new ResponseEntity<>(requestService.updateEventRequestStatus(userId, eventId, request),
                HttpStatus.OK);
    }
}

