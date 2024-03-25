package ru.practicum.ewm_main_service.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.request.dto.ParticipationRequestDto;
import ru.practicum.ewm_main_service.request.entity.ParticipationRequest;
import ru.practicum.ewm_main_service.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm_main_service.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по ParticipationRequest (заявкам на участие в событии)
 */
@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {
    private final RequestService requestService;
    private final ParticipationRequestMapper participationRequestMapper;

    /**
     * метод получения информации о заявках текущего пользователя на участие в чужих событиях
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<ParticipationRequestDto>> findRequestsByUserId(
            @PathVariable("userId") @Positive Long userId) {
        log.info("Выполняется запрос на поиск заявок на участие...");
        List<ParticipationRequest> foundedRequests = requestService.findRequestsByUserId(userId);
        return foundedRequests.isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(foundedRequests
                .stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    /**
     * метод создания запроса от текущего пользователя на участие в событии
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<ParticipationRequestDto> createRequest(
            @PathVariable("userId") @Positive Long userId,
            @RequestParam(value = "eventId") @Positive Long eventId) {
        log.info("Выполняется запрос на создание заявки на участие в событиях...");
        return new ResponseEntity<>(participationRequestMapper.toParticipationRequestDto(
                requestService.createRequest(userId, eventId)),
                HttpStatus.CREATED);
    }

    /**
     * Отмена запроса текущего пользователя на участие в событии
     */
    @PatchMapping(path = "/{requestId}/cancel", headers = "Accept=application/json")
    public ResponseEntity<ParticipationRequestDto> cancelRequestByRequester(
            @PathVariable("userId") @Positive Long userId,
            @PathVariable("requestId") @Positive Long requestId) {
        log.info("Выполняется запрос на отмену заявки на участие в событиях...");
        return new ResponseEntity<>(participationRequestMapper.toParticipationRequestDto(
                requestService.cancelRequestByRequester(userId, requestId)),
                HttpStatus.OK);
    }
}

