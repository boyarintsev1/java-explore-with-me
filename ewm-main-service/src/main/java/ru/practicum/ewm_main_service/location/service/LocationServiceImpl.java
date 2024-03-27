package ru.practicum.ewm_main_service.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.exception.NotFoundException;
import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

/**
 * класс для работы с данными о локациях Location при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Location> findLocations(Integer from, Integer size) {
        log.info("Выполняется запрос на получение локаций.");
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return locationRepository.findAll(page);
    }

    @Transactional(readOnly = true)
    @Override
    public Location findLocationById(Long locId) {
        log.info("Выполняется запрос на получение указанной локации...");
        return locationRepository.findById(locId)
                .orElseThrow(() -> new NotFoundException("Локация не найдена или недоступна", locId, "Category"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Location> findNearestLocations(Float lat, Float lon, Float radius) {
        log.info("Запрос на получение ближайших локаций к указанной локации.");
        return locationRepository.findNearestLocations(lat, lon, radius);
    }

    @Transactional
    @Override
    public Location createLocation(Location location) {
        Optional<Location> foundedLocation = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        return foundedLocation.orElseGet(() -> locationRepository.save(location));
    }

    @Transactional
    @Override
    public Location updateLocation(Long locId, Location location) {
        Location dbLocation = locationRepository.findById(locId)
                .orElseThrow(() -> new NotFoundException("Локация не найдена или недоступна", locId, "Location"));
        if (location.getTitle() != null) {
            dbLocation.setTitle(location.getTitle());
        }
        if (location.getDescription() != null) {
            dbLocation.setDescription(location.getDescription());
        }
        if (location.getLat() != null) {
            dbLocation.setLat(location.getLat());
        }
        if (location.getLon() != null) {
            dbLocation.setLon(location.getLon());
        }
        log.info("Запрос на изменение данных локации");
        return locationRepository.save(dbLocation);
    }

    @Transactional
    @Override
    public void deleteLocation(Long locId) {
        if (locationRepository.findById(locId).isEmpty()) {
            throw new NotFoundException("Локация не найдена или недоступна", locId, "Location");
        } else {
            locationRepository.deleteById(locId);
        }
        log.info("Локация удалена");
    }
}

