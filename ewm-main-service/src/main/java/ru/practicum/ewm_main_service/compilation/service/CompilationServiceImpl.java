package ru.practicum.ewm_main_service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;
import ru.practicum.ewm_main_service.compilation.repository.CompilationRepository;
import ru.practicum.ewm_main_service.event.repository.EventRepository;
import ru.practicum.ewm_main_service.exception.NotFoundException;

/**
 * класс для работы с данными о подборках Compilation при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Compilation> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Выполняется запрос на получение всех подборок");
        Pageable page = PageRequest.of(from, size);
        return pinned == null
                ? compilationRepository.findAllCompilations(page)
                : compilationRepository.findPinnedCompilations(pinned, page);
    }

    @Transactional(readOnly = true)
    @Override
    public Compilation findCompilationById(Long compId) {
        log.info("Выполняется запрос на получение указанной подборки");
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена или недоступна", compId, "Compilation"));
    }

    @Transactional
    @Override
    public Compilation createCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public Compilation updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation dbCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена или недоступна", compId, "Compilation"));
        if (updateCompilationRequest.getEvents() != null) {
            dbCompilation.setEvents(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        }
        if (updateCompilationRequest.getPinned() != null) {
            dbCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            dbCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        log.info("Обновлен объект : {}", dbCompilation);
        return compilationRepository.save(dbCompilation);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException("Подборка не найдена или недоступна", compId, "Compilation");
        } else {
            compilationRepository.deleteById(compId);
        }
    }
}

