package ru.practicum.ewm_main_service.location.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;
import ru.practicum.ewm_main_service.location.dto.LocationFullDto;
import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.mapper.LocationMapper;
import ru.practicum.ewm_main_service.location.service.LocationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-контроллер по Location (локациям) - публичный
 */
@RestController
@Slf4j
@RequestMapping(path = "/locations")
@RequiredArgsConstructor
@Validated
public class PublicLocationController {
    private final LocationService locationService;
    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * метод получения информации обо всех локациях
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<LocationFullDto>> findLocations(
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Выполняется запрос на поиск всех локаций...");
        Page<Location> foundedLocations = locationService.findLocations(from, size);
        return foundedLocations.isEmpty()
                ? new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK)
                : new ResponseEntity<>(foundedLocations
                .map(LocationMapper::toLocationFullDto)
                .getContent(), HttpStatus.OK);
    }

    /**
     * метод получения информации о локации по её ID
     */
    @GetMapping(path = "/{locId}", headers = "Accept=application/json")
    public ResponseEntity<LocationFullDto> findLocationById(@PathVariable("locId") Long locId) {
        log.info("Выполняется запрос на поиск локации по ID...");
        Location foundedLocation = locationService.findLocationById(locId);
        return new ResponseEntity<>(LocationMapper.toLocationFullDto(foundedLocation),
                HttpStatus.OK);
    }


    /**
     * метод получения информации о событиях Event в указанной локации.
     */
    @GetMapping(path = "/events", headers = "Accept=application/json")
    public ResponseEntity<List<EventShortDto>> findEventsInLocation(
            @RequestParam(value = "lat") @Min(-90) @Max(90) Float lat,
            @RequestParam(value = "lon") @Min(-180) @Max(180) Float lon,
            @RequestParam(value = "radius") @Positive Float radius,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("Выполняется запрос на поиск событий в указанной локации...");
        List<Location> foundedNearestLocations = locationService.findNearestLocations(lat, lon, radius);
        return foundedNearestLocations.isEmpty()
                ? new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK)
                : new ResponseEntity<>(eventService.findEventsInLocation(foundedNearestLocations, from, size, request)
                .map(eventMapper::toEventShortDto)
                .getContent(), HttpStatus.OK);
    }
}

