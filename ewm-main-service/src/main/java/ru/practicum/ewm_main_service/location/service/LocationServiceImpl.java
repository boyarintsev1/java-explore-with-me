package ru.practicum.ewm_main_service.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.repository.LocationRepository;

import java.util.Optional;

/**
 * класс для работы с данными о локациях Location при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public Location createLocation(Location location) {
        Optional<Location> foundedLocation = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        return foundedLocation.orElseGet(() -> locationRepository.save(location));
    }
}

