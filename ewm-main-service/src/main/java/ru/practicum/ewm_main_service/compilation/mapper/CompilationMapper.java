package ru.practicum.ewm_main_service.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;
import ru.practicum.ewm_main_service.event.mapper.EventMapper;
import ru.practicum.ewm_main_service.event.service.EventService;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Класс CompilationMapper позволяет преобразовать NewCompilationDto в сущность Compilation.
 */
@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventService eventService;
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto == null) {
            return null;
        }
        return Compilation
                .builder()
                .events(newCompilationDto.getEvents() != null
                        ? newCompilationDto.getEvents().stream()
                        .map(eventService::findEventByIdNoViews)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .pinned(newCompilationDto.getPinned() != null
                        ? newCompilationDto.getPinned()
                        : false)
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return CompilationDto
                .builder()
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}


