package ru.practicum.ewm_main_service.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.ewm_main_service.location.entity.Location;
import ru.practicum.ewm_main_service.location.repository.LocationRepository;

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
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon()).isEmpty()
                ? locationRepository.save(location)
                : locationRepository.findByLatAndLon(location.getLat(), location.getLon()).get();
    }
}

