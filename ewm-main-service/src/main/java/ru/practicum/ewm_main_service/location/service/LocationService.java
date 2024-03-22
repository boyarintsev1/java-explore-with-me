package ru.practicum.ewm_main_service.location.service;

import ru.practicum.ewm_main_service.location.entity.Location;

/**
 * интерфейс для работы с данными о локациях Location
 */
public interface LocationService {

    /**
     * метод создания новой локации
     */
    Location createLocation(Location location);
}
