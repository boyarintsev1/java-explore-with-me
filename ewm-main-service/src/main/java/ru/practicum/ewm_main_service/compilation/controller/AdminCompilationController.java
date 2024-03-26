package ru.practicum.ewm_main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;
import ru.practicum.ewm_main_service.compilation.mapper.CompilationMapper;
import ru.practicum.ewm_main_service.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * Класс-контроллер по Compilation (админ)
 */
@RestController
@Slf4j
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        log.info("Выполняется запрос на добавление подборки событий...");
        Compilation newCompilation = compilationService.createCompilation(compilation);
        return new ResponseEntity<>(compilationMapper.toCompilationDto(newCompilation), HttpStatus.CREATED);
    }

    /**
     * метод обновления данных о подборке событий.
     */
    @PatchMapping(path = "/{compId}", headers = "Accept=application/json")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable("compId") @Positive Long compId,
            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Выполняется запрос на обновление подборки событий...");
        Compilation updatedCompilation =  compilationService.updateCompilationById(compId, updateCompilationRequest);
        return new ResponseEntity<>(compilationMapper.toCompilationDto(updatedCompilation), HttpStatus.OK);
    }

    /**
     * метод удаления подборки
     */
    @DeleteMapping("/{compId}")
    public ResponseEntity<HttpStatus> deleteCompilation(@PathVariable("compId") Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("Выполняется запрос на удаление подборки событий...");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

