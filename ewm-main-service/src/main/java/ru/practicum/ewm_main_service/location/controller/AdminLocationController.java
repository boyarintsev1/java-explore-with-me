package ru.practicum.ewm_main_service.location.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.location.dto.LocationFullDto;
import ru.practicum.ewm_main_service.location.dto.NewLocationDto;
import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.mapper.LocationMapper;
import ru.practicum.ewm_main_service.location.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * Класс-контроллер по Location (локациям)
 */
@RestController
@Slf4j
@RequestMapping(path = "/admin/locations")
@RequiredArgsConstructor
@Validated
public class AdminLocationController {
    private final LocationService locationService;

    /**
     * метод создания новой локации администратором
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<LocationFullDto> createLocationByAdmin(
            @Valid @RequestBody NewLocationDto newLocationDto) {
        Location location = LocationMapper.toLocation(newLocationDto);
        log.info("Выполняется запрос на создание локации...");
        Location newlocation = locationService.createLocation(location);
        return new ResponseEntity<>(LocationMapper.toLocationFullDto(newlocation),
                HttpStatus.CREATED);
    }

    /**
     * метод обновления данных для локации с указанным ID
     */
    @PatchMapping(path = "/{locId}", headers = "Accept=application/json")
    public ResponseEntity<LocationFullDto> updateLocationByAdmin(@PathVariable("locId") @Positive Long locId,
                                                                 @RequestBody NewLocationDto newLocationDto) {
        Location location = LocationMapper.toLocation(newLocationDto);
        log.info("Выполняется запрос на изменение данных локации...");
        Location updatedLocation = locationService.updateLocation(locId, location);
        return new ResponseEntity<>(LocationMapper.toLocationFullDto(updatedLocation),
                HttpStatus.OK);
    }

    /**
     * метод удаления локации
     */
    @DeleteMapping("/{locId}")
    public ResponseEntity<HttpStatus> deleteLocation(@PathVariable("locId") Long locId) {
        locationService.deleteLocation(locId);
        log.info("Создание запроса на удаление локации...");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

