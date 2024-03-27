package ru.practicum.ewm_main_service.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.location.entity.Location;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "SELECT e.* FROM public.EVENTS as e " +
            " WHERE e.initiator IN (SELECT u.id FROM public.USERS as u WHERE u.id = ?1) ORDER BY e.id",
            nativeQuery = true)
    Page<Event> findAllEventsOfCurrentUser(Long userId, Pageable page);

    @Query(value = "SELECT e.* FROM public.EVENTS as e " +
            " WHERE e.id = ?1 AND e.initiator IN (SELECT u.id FROM public.USERS as u WHERE u.id = ?2) ORDER BY e.id",
            nativeQuery = true)
    Optional<Event> findEventOfCurrentUserById(Long eventId, Long userId);

    @Query(value = "SELECT e.* FROM public.EVENTS as e " +
            " WHERE e.location IN ?1 ORDER BY e.id",
            nativeQuery = true)
    Page<Event> findEventsInLocation(Object[] foundedNearestLocations, Pageable page);

    Event findByLocation(Location location);
}

