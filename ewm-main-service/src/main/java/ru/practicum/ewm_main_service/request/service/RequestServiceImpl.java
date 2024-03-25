package ru.practicum.ewm_main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.enums.State;
import ru.practicum.ewm_main_service.enums.Status;
import ru.practicum.ewm_main_service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm_main_service.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.event.repository.EventRepository;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.exception.ForbiddenException;
import ru.practicum.ewm_main_service.exception.NotFoundException;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;
import ru.practicum.ewm_main_service.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm_main_service.request.repository.RequestRepository;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * класс для работы с данными о запросах Request при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;
    private final ParticipationRequestMapper participationRequestMapper;
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

    @Transactional(readOnly = true)
    @Override
    public Long countRequestsByEventIdAndStatus(Long eventId, Status status) {
        return requestRepository.countAllByEventAndStatus(eventId, status.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequest> findRequestsByUserId(Long userId) {
        return requestRepository.findRequestsByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequest> findRequestsToEventOfCurrentUser(Long userId, Long eventId) {
        return requestRepository.findRequestsToEventOfCurrentUser(userId, eventId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ParticipationRequest createRequest(Long userId, Long eventId) {
        Event dbEvent = eventService.findEventByIdNoViews(eventId);
        if (dbEvent.getInitiator().getId().equals(userId))
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии",
                    HttpStatus.CONFLICT);
        if (dbEvent.getState() != State.PUBLISHED)
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии",
                    HttpStatus.CONFLICT);
        if ((dbEvent.getParticipantLimit() != 0)
                && (dbEvent.getConfirmedRequests().equals(dbEvent.getParticipantLimit())))
            throw new ForbiddenException("У события достигнут лимит запросов на участие",
                    HttpStatus.CONFLICT);
        ParticipationRequest participationRequest = ParticipationRequest
                .builder()
                .created((LocalDateTime.parse(formatter.format(LocalDateTime.now()), formatter)))
                .event(dbEvent)
                .requester(userService.findUserById(userId))
                .status(dbEvent.getParticipantLimit().equals(0L)
                        ? Status.CONFIRMED
                        : Status.PENDING)
                .build();
        dbEvent.setConfirmedRequests(dbEvent.getConfirmedRequests() + 1);
        eventRepository.save(dbEvent);
        return requestRepository.save(participationRequest);
    }

    @Transactional
    @Override
    public ParticipationRequest cancelRequestByRequester(Long userId, Long requestId) {
        ParticipationRequest dbRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден или недоступен", requestId,
                        "ParticipationRequest"));
        if (dbRequest.getStatus() == Status.CONFIRMED)
            throw new ForbiddenException("It is not possible to cancel request with status CONFIRMED",
                    HttpStatus.CONFLICT);
        if (dbRequest.getStatus() == Status.PENDING) {
            dbRequest.setStatus(Status.CANCELED);
        }
        log.info("Обновлен объект : {}", dbRequest);
        return requestRepository.save(dbRequest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest request) {
        ParticipationRequest dbRequest;
        Event dbEvent = eventService.findEventByIdNoViews(eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());

        for (Long i : request.getRequestIds()) {
            dbRequest = requestRepository.findById(i)
                    .orElseThrow(() -> new NotFoundException("Запрос не найден или недоступен", i,
                            "ParticipationRequest"));
            if (dbRequest.getStatus() != Status.PENDING)
                throw new ForbiddenException("Request must have status PENDING", HttpStatus.CONFLICT);

            if ((dbEvent.getConfirmedRequests().equals(dbEvent.getParticipantLimit()))
                    && dbEvent.getParticipantLimit() != 0) {
                dbRequest.setStatus(Status.REJECTED);
                requestRepository.save(dbRequest);
            } else if (dbEvent.getRequestModeration().equals(false) || dbEvent.getParticipantLimit() == 0) {
                dbRequest.setStatus(Status.CONFIRMED);
                requestRepository.save(dbRequest);
            } else {
                dbRequest.setStatus(Status.valueOf(request.getStatus()));
                requestRepository.save(dbRequest);
                if (dbEvent.getConfirmedRequests() < (dbEvent.getParticipantLimit())) {
                    dbEvent.setConfirmedRequests(dbEvent.getConfirmedRequests() + 1);
                } else {
                    throw new ForbiddenException("The participant limit has been reached", HttpStatus.CONFLICT);
                }
                if (Status.valueOf(request.getStatus()) == Status.CONFIRMED) {
                    result.getConfirmedRequests().add(participationRequestMapper.toParticipationRequestDto(dbRequest));
                }
                if (Status.valueOf(request.getStatus()) == Status.REJECTED) {
                    result.getRejectedRequests().add(participationRequestMapper.toParticipationRequestDto(dbRequest));
                }
                log.info("Обновлен объект : {}", dbRequest);
            }
        }
        eventRepository.save(dbEvent);
        return result;
    }
}

