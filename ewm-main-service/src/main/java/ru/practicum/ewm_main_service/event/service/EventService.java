package ru.practicum.ewm_main_service.event.service;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main_service.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_main_service.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm_main_service.event.entity.Event;

import javax.servlet.http.HttpServletRequest;

/**
 * интерфейс для работы с данными о событиях Event
 */
public interface EventService {

    /**
     * метод получения событий, добавленных текущим пользователем
     */
    Page<Event> findAllEventsOfCurrentUser(Long userId, Integer from, Integer size);

    /**
     * метод получения полной информации о событии, добавленном текущим пользователем.
     */
    Event findEventOfCurrentUserById(Long userId, Long eventId);

    /**
     * метод получения событий с возможностью фильтрации.
     */
    Page<Event> findEvents(String text, Integer[] categories, Boolean paid, String rangeStart, String rangeEnd,
                           Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    Page<Event> findEvents(Long[] users,String[] states, Integer[] categories, String rangeStart, String rangeEnd,
                           Integer from, Integer size, HttpServletRequest request);

    /**
     * получение подробной информации об опубликованном событии по его идентификатору
     */
    Event findEventById(Long eventId, HttpServletRequest request);

    /**
     * получение информации об опубликованном событии по его идентификатору (без просмотра события)
     */
    Event findEventByIdNoViews(Long eventId);

    /**
     * метод создания новой категории
     */
    Event createEvent(Event event);

    /**
     * Изменение события добавленного текущим пользователем
     */
    Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * редактирование данных события и его статуса (отклонение/публикация) админом.
     */
    Event updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

}
