package ru.practicum.ewm_main_service.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.location.entity.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(float lat, float lon);

    @Query(value = "SELECT l.*, distance(?1, ?2, l.lat, l.lon) FROM public.Locations as l " +
            " GROUP BY l.id HAVING distance(?1, ?2, l.lat, l.lon)<?3  ORDER BY l.id",
            nativeQuery = true)
    List<Location> findNearestLocations(float lat, float lon, Float radius);
}



