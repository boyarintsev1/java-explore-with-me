package ru.practicum.ewm_main_service.location.service;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main_service.location.entity.Location;

import java.util.List;

/**
 * интерфейс для работы с данными о локациях Location
 */
public interface LocationService {

    /**
     * метод получения информации обо всех локациях
     */
    Page<Location> findLocations(Integer from, Integer size);

    /**
     * метод получения информации о локации по её ID
     */
    Location findLocationById(Long locId);

    /**
     * метод получения информации о ближайших локациях к указанной локации.
     */
    List<Location> findNearestLocations(Float lat, Float lon, Float radius);

    /**
     * метод создания новой локации
     */
    Location createLocation(Location location);

    /**
     * метод обновления данных для локации с указанным ID
     */
    Location updateLocation(Long locId, Location location);

    /**
     * метод удаления локации
     */
    void deleteLocation(Long locId);
}
