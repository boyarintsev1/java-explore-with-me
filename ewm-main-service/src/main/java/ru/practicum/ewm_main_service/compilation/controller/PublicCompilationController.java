package ru.practicum.ewm_main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;
import ru.practicum.ewm_main_service.compilation.mapper.CompilationMapper;
import ru.practicum.ewm_main_service.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс-контроллер по Compilation публичный (public)
 */
@RestController
@Slf4j
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    /**
     * метод получения подборок событий;
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<CompilationDto>> findAllCompilations(
            @RequestParam(value = "pinned", required = false) Boolean pinned,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Выполняется запрос на получение информации о подборках событий.");
        Page<Compilation> foundedCompilations = compilationService.findAllCompilations(pinned, from, size);
        return foundedCompilations.isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(foundedCompilations
                .map(compilationMapper::toCompilationDto)
                .getContent(), HttpStatus.OK);
    }

    /**
     * метод получения подборки событий по его ID
     */
    @GetMapping(path = "/{compId}", headers = "Accept=application/json")
    public ResponseEntity<CompilationDto> findCompilationById(@PathVariable("compId") Long compId) {
        log.info("Выполняется запрос на поиск подборки событий...");
        Compilation foundedCompilation = compilationService.findCompilationById(compId);
        return new ResponseEntity<>(compilationMapper.toCompilationDto(foundedCompilation),
                HttpStatus.OK);
    }
}

