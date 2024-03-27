package ru.practicum.ewm_main_service.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.location.dto.LocationFullDto;
import ru.practicum.ewm_main_service.location.dto.LocationShortDto;
import ru.practicum.ewm_main_service.location.dto.NewLocationDto;
import ru.practicum.ewm_main_service.location.entity.Location;

/**
 * Класс LocationMapper позволяет преобразовать входящие данные NewLocationDto в сущность Location, а также
 * преобразовать Location в нужный формат возврата данных LocationFullDto и LocationShortDto.
 */
@UtilityClass
public class LocationMapper {

    public static Location toLocation(NewLocationDto newLocationDto) {
        if (newLocationDto == null) {
            return null;
        }
        String title = newLocationDto.getTitle();
        String description = newLocationDto.getDescription();
        return Location
                .builder()
                .title(title)
                .description(description)
                .lat(newLocationDto.getLat())
                .lon(newLocationDto.getLon())
                .build();
    }

    public static LocationFullDto toLocationFullDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationFullDto
                .builder()
                .id(location.getId())
                .title(location.getTitle())
                .description(location.getDescription())
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static LocationShortDto toLocationShortDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationShortDto
                .builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
