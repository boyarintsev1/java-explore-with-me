package ru.practicum.ewm_main_service.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_main_service.category.mapper.CategoryMapper;
import ru.practicum.ewm_main_service.category.service.CategoryService;
import ru.practicum.ewm_main_service.enums.State;
import ru.practicum.ewm_main_service.enums.Status;
import ru.practicum.ewm_main_service.event.dto.EventFullDto;
import ru.practicum.ewm_main_service.event.dto.EventShortDto;
import ru.practicum.ewm_main_service.event.dto.NewEventDto;
import ru.practicum.ewm_main_service.event.entity.Event;
import ru.practicum.ewm_main_service.location.mapper.LocationMapper;
import ru.practicum.ewm_main_service.location.service.LocationService;
import ru.practicum.ewm_main_service.request.service.RequestService;
import ru.practicum.ewm_main_service.user.mapper.UserMapper;
import ru.practicum.ewm_main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс EventMapper позволяет преобразовать входящие данные в сущность Event, а также преобразовать Event
 * в нужный формат возврата данных EventFullDto или EventShortDto.
 */
@Component
@RequiredArgsConstructor
public class EventMapper {
    private final RequestService requestService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto == null) {
            return null;
        }
        return Event
                .builder()
                .annotation(newEventDto.getAnnotation())
                .category(categoryService.findCategoryById(newEventDto.getCategory()))
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.parse(dtf.format(LocalDateTime.now()), dtf))
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), dtf))
                .initiator(userService.findUserById(userId))
                .location(locationService.createLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false)
                .participantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0)
                .requestModeration(newEventDto.getRequestModeration() != null
                        ? newEventDto.getRequestModeration()
                        : true)
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();

    }

    public EventFullDto toEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventFullDto
                .builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(requestService.countRequestsByEventIdAndStatus(event.getId(), Status.CONFIRMED))
                .createdOn(dtf.format(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(dtf.format(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationShortDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() != null
                        ? dtf.format(event.getPublishedOn())
                        : "не опубликовано")
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventShortDto
                .builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(requestService.countRequestsByEventIdAndStatus(event.getId(), Status.CONFIRMED))
                .eventDate(dtf.format(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}



