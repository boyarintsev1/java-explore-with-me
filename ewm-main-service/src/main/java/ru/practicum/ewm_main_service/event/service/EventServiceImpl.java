package ru.practicum.ewm_main_service.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.category.service.CategoryService;
import ru.practicum.ewm_main_service.enums.SortType;
import ru.practicum.ewm_main_service.enums.State;
import ru.practicum.ewm_main_service.enums.StateAction;
import ru.practicum.ewm_main_service.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_main_service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.event.entity.QEvent;
import ru.practicum.ewm_main_service.event.repository.EventRepository;
import ru.practicum.ewm_main_service.exception.BadRequestException;
import ru.practicum.ewm_main_service.exception.ForbiddenException;
import ru.practicum.ewm_main_service.exception.NotFoundException;
import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.service.LocationService;
import ru.practicum.ewm_stats.dto.EndpointHitRequestDto;
import ru.practicum.ewm_stats_client.client.EndpointHitClient;
import ru.practicum.ewm_stats_client.controller.ViewStatsController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * класс для работы с данными о событиях Event при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EndpointHitClient endpointHitClient;
    private final ViewStatsController viewStatsController;
    private final CategoryService categoryService;
    private final LocationService locationService;

    String message;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    @Override
    public Page<Event> findAllEventsOfCurrentUser(Long userId, Integer from, Integer size) {
        log.info("Выполняется запрос на получение событий текущего пользователя.");
        Pageable page = PageRequest.of(from, size);
        return eventRepository.findAllEventsOfCurrentUser(userId, page);
    }

    @Transactional(readOnly = true)
    @Override
    public Event findEventOfCurrentUserById(Long userId, Long eventId) {
        log.info("Выполняется запрос на полного описания события для текущего пользователя...");
        return eventRepository.findEventOfCurrentUserById(eventId, userId).orElseThrow(() ->
                new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Page<Event> findEvents(String text, Integer[] categories, Boolean paid, String rangeStart, String rangeEnd,
                                  Boolean onlyAvailable, String sort, Integer from, Integer size,
                                  HttpServletRequest request) {
        Pageable page;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (text != null && !text.isBlank() && !text.isEmpty()) {
            predicates.add(QEvent.event.annotation.containsIgnoreCase(text.trim())
                    .or(QEvent.event.description.containsIgnoreCase(text.trim())));
        }
        if (categories != null && categories.length != 0) {
            predicates.add(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            predicates.add(QEvent.event.paid.eq(paid));
        }
        if (rangeStart == null && rangeEnd == null) {
            predicates.add(QEvent.event.eventDate.goe(LocalDateTime.now()));
        } else if (rangeStart == null && rangeEnd != null) {
            throw new BadRequestException("rangeEnd without rangeStart");
        } else if (rangeStart != null && rangeEnd == null) {
            throw new BadRequestException("rangeStart without rangeEnd");
        } else if (LocalDateTime.parse(rangeStart, dtf).isAfter(LocalDateTime.parse(rangeEnd, dtf))) {
            throw new BadRequestException("rangeStart is after rangeEnd");
        } else {
            predicates.add(QEvent.event.eventDate
                    .between(LocalDateTime.parse(rangeStart, dtf), LocalDateTime.parse(rangeEnd, dtf)));
        }
        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(QEvent.event.confirmedRequests.loe(QEvent.event.participantLimit));
        }
        if (sort != null && sort.equals(String.valueOf(SortType.EVENT_DATE))) {
            page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        } else if (sort != null && sort.equals(String.valueOf(SortType.VIEWS))) {
            page = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "views"));
        } else {
            page = PageRequest.of(from, size);
        }
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        log.info("Выполняется запрос на получение событий");
        Page<Event> eventsFound = eventRepository.findAll(result, page);

        for (Event event : eventsFound.getContent()) {
            boolean uniqueIp = Boolean.parseBoolean(Objects.requireNonNull(viewStatsController
                    .checkUniqueIpForUri(request.getRemoteAddr(), request.getRequestURI()).getBody()).toString());
            if (uniqueIp) {
                event.setViews(event.getViews() + 1);
            }
        }

        endpointHitClient.createEndpointHit(EndpointHitRequestDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());

        return eventsFound;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public Page<Event> findEvents(Long[] users, String[] states, Integer[] categories, String rangeStart, String rangeEnd,
                                  Integer from, Integer size, HttpServletRequest request) {
        Pageable page = PageRequest.of(from, size);
        List<BooleanExpression> predicates = new ArrayList<>();
        if (users != null && users.length != 0) {
            predicates.add(QEvent.event.initiator.id.in(users));
        }
        if (states != null && states.length > 0) {
            State[] enumStates = new State[states.length];
            for (int i = 0; i < states.length; i++) {
                enumStates[i] = State.valueOf(states[i]);
            }
            predicates.add(QEvent.event.state.in(enumStates));
        }
        if (categories != null && categories.length != 0) {
            predicates.add(QEvent.event.category.id.in(categories));
        }
        if (rangeStart == null && rangeEnd == null) {
            predicates.add(QEvent.event.eventDate.goe(LocalDateTime.now()));
        } else if (rangeStart == null && rangeEnd != null) {
            throw new ForbiddenException("rangeEnd without rangeStart", HttpStatus.CONFLICT);
        } else if (rangeStart != null && rangeEnd == null) {
            throw new ForbiddenException("rangeStart without rangeEnd", HttpStatus.CONFLICT);
        } else if (LocalDateTime.parse(rangeStart, dtf).isAfter(LocalDateTime.parse(rangeEnd, dtf))) {
            throw new ForbiddenException("rangeStart is after rangeEnd", HttpStatus.CONFLICT);
        } else {
            predicates.add(QEvent.event.eventDate
                    .between(LocalDateTime.parse(rangeStart, dtf), LocalDateTime.parse(rangeEnd, dtf)));
        }
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        log.info("Выполняется запрос на получение событий");
        return eventRepository.findAll(result, page);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Event findEventById(Long eventId, HttpServletRequest request) {
        log.info("Выполняется запрос на получение события");
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
        if (event.getState() != State.PUBLISHED)
            throw new NotFoundException("Event must be published", eventId, "Event");

        boolean uniqueIp = Boolean.parseBoolean(Objects.requireNonNull(viewStatsController
                .checkUniqueIpForUri(request.getRemoteAddr(), request.getRequestURI()).getBody()).toString());
        if (uniqueIp) {
            event.setViews(event.getViews() + 1);
        }

        endpointHitClient.createEndpointHit(EndpointHitRequestDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());
        log.info("Создан EndPointHit c eventId = " + event.getId());
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public Event findEventByIdNoViews(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
    }

    @Transactional
    @Override
    public Event createEvent(Event event) {
        validateEventDate(event.getEventDate(), 2);
        log.info("Создан объект : {}", event);
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event dbEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
        if (!dbEvent.getInitiator().getId().equals(userId))
            throw new ForbiddenException("Only the initiator can update this event.", HttpStatus.CONFLICT);
        if (dbEvent.getState() == State.PUBLISHED)
            throw new ForbiddenException("Only pending or canceled events can be changed.", HttpStatus.CONFLICT);

        if ((updateEventUserRequest.getAnnotation() != null)
                && (!updateEventUserRequest.getAnnotation().equals(dbEvent.getAnnotation()))) {
            dbEvent.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if ((updateEventUserRequest.getCategory() != null)
                && (!updateEventUserRequest.getCategory().equals(dbEvent.getCategory().getId()))) {
            dbEvent.setCategory(categoryService.findCategoryById(updateEventUserRequest.getCategory()));
        }
        if ((updateEventUserRequest.getDescription() != null)
                && (!updateEventUserRequest.getDescription().equals(dbEvent.getDescription()))) {
            dbEvent.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            validateEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate(), dtf), 2);
            if (!(LocalDateTime.parse(updateEventUserRequest.getEventDate(), dtf)
                    .equals(dbEvent.getEventDate()))) {
                dbEvent.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate(), dtf));
            }
        }
        if ((updateEventUserRequest.getLocation() != null)
                && (!updateEventUserRequest.getLocation().equals(dbEvent.getLocation()))) {
            dbEvent.setLocation(locationService.createLocation(updateEventUserRequest.getLocation()));
        }
        if ((updateEventUserRequest.getPaid() != null)
                && (!updateEventUserRequest.getPaid().equals(dbEvent.getPaid()))) {
            dbEvent.setPaid(updateEventUserRequest.getPaid());
        }
        if ((updateEventUserRequest.getParticipantLimit() != null)
                && (!updateEventUserRequest.getParticipantLimit().equals(dbEvent.getParticipantLimit()))) {
            dbEvent.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if ((updateEventUserRequest.getRequestModeration() != null)
                && (!updateEventUserRequest.getRequestModeration().equals(dbEvent.getRequestModeration()))) {
            dbEvent.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW.toString())) {
                dbEvent.setState(State.PENDING);
            }
            if (updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW.toString())) {
                dbEvent.setState(State.CANCELED);
            }
        }
        if ((updateEventUserRequest.getTitle() != null)
                && (!updateEventUserRequest.getTitle().equals(dbEvent.getTitle()))) {
            dbEvent.setTitle(updateEventUserRequest.getTitle());
        }
        dbEvent.setPublishedOn(null);
        log.info("Обновлен объект : {}", dbEvent);
        return eventRepository.save(dbEvent);
    }

    @Transactional
    @Override
    public Event updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event dbEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
        if (dbEvent.getState() != State.PENDING)
            throw new ForbiddenException(String.format("Cannot publish the event because it's not in the right " +
                    "state: %s", dbEvent.getState()), HttpStatus.CONFLICT);
        if ((updateEventAdminRequest.getAnnotation() != null)
                && (!updateEventAdminRequest.getAnnotation().equals(dbEvent.getAnnotation()))) {
            dbEvent.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if ((updateEventAdminRequest.getCategory() != null)
                && (!updateEventAdminRequest.getCategory().equals(dbEvent.getCategory().getId()))) {
            dbEvent.setCategory(categoryService.findCategoryById(updateEventAdminRequest.getCategory()));
        }
        if ((updateEventAdminRequest.getDescription() != null)
                && (!updateEventAdminRequest.getDescription().equals(dbEvent.getDescription()))) {
            dbEvent.setDescription(updateEventAdminRequest.getDescription());
        }
        if ((updateEventAdminRequest.getEventDate() != null)) {
            validateEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), dtf), 1);
            if (!(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), dtf)
                    .equals(dbEvent.getEventDate()))) {
                dbEvent.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), dtf));
            }
        }
        if ((updateEventAdminRequest.getLocation() != null)
                && (!updateEventAdminRequest.getLocation().equals(dbEvent.getLocation()))) {
            dbEvent.setLocation(locationService.createLocation(updateEventAdminRequest.getLocation()));
        }
        if ((updateEventAdminRequest.getPaid() != null)
                && (!updateEventAdminRequest.getPaid().equals(dbEvent.getPaid()))) {
            dbEvent.setPaid(updateEventAdminRequest.getPaid());
        }
        if ((updateEventAdminRequest.getParticipantLimit() != null)
                && (!updateEventAdminRequest.getParticipantLimit().equals(dbEvent.getParticipantLimit()))) {
            dbEvent.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if ((updateEventAdminRequest.getRequestModeration() != null)
                && (!updateEventAdminRequest.getRequestModeration().equals(dbEvent.getRequestModeration()))) {
            dbEvent.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT.toString())) {
                dbEvent.setPublishedOn(LocalDateTime.parse(dtf.format(LocalDateTime.now()), dtf));
                dbEvent.setState(State.PUBLISHED);
            }
            if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT.toString())) {
                dbEvent.setPublishedOn(null);
                dbEvent.setState(State.CANCELED);
            }
        }
        if ((updateEventAdminRequest.getTitle() != null)
                && (!updateEventAdminRequest.getTitle().equals(dbEvent.getTitle()))) {
            dbEvent.setTitle(updateEventAdminRequest.getTitle());
        }
        log.info("Обновлен объект : {}", dbEvent);
        return eventRepository.save(dbEvent);
    }

    public void validateEventDate(LocalDateTime eventDate, Integer timeGap) {
        LocalDateTime timestamp = LocalDateTime.now();
        if (eventDate.isBefore(timestamp))
            throw new BadRequestException("EventDate is in past.");
        if (eventDate.isBefore(timestamp.plusHours(timeGap))) {
            message = String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: %s", eventDate);
            log.error(String.format("Событие не удовлетворяет правилам создания. Дата и время на которые намечено " +
                    "событие не может быть раньше, чем через %s час(а) от текущего момента", timeGap));
            throw new ForbiddenException(message, HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public Event updateConfirmedRequestsForEvent(Long eventId) {
        Event dbEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно", eventId, "Event"));
        if ((dbEvent.getParticipantLimit() != 0)
                && (dbEvent.getConfirmedRequests()).equals(dbEvent.getParticipantLimit())) {
            log.warn("The quantity of confirmed requests cannot be more than participant limit");
            throw new ForbiddenException("The quantity of confirmed requests cannot be more than participant limit",
                    HttpStatus.CONFLICT);
        } else {
            log.info("Количество просмотров увеличено на 1:");
            dbEvent.setConfirmedRequests(dbEvent.getConfirmedRequests() + 1);
        }
        return eventRepository.save(dbEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Page<Event> findEventsInLocation(List<Location> foundedNearestLocations, Integer from, Integer size,
                                            HttpServletRequest request) {
        log.info("Выполняется запрос на получение событий в указанной локации.");
        Pageable page = PageRequest.of(from, size);
        Page<Event> eventsFound = eventRepository.findEventsInLocation(foundedNearestLocations.toArray(), page);

        for (Event event : eventsFound.getContent()) {
            boolean uniqueIp = Boolean.parseBoolean(Objects.requireNonNull(viewStatsController
                    .checkUniqueIpForUri(request.getRemoteAddr(), request.getRequestURI()).getBody()).toString());
            if (uniqueIp) {
                event.setViews(event.getViews() + 1);
            }
        }

        endpointHitClient.createEndpointHit(EndpointHitRequestDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build());

        return eventsFound;
    }

}

