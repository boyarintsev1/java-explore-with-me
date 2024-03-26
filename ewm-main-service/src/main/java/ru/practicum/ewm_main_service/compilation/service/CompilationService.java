package ru.practicum.ewm_main_service.compilation.service;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;

/**
 * интерфейс для работы с данными о подборках запросах (Compilation)
 */
public interface CompilationService {

    /**
     * метод получения подборок событий.
     */
    Page<Compilation> findAllCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * метод получения подборки событий по его ID.
     */
    Compilation findCompilationById(Long compId);

    /**
     * метод добавления новой подборки (подборка может не содержать событий).
     */
    Compilation createCompilation(Compilation compilation);

    /**
     * метод обновления данных о подборке событий.
     */
    Compilation updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest);

    /**
     * метод удаления подборки.
     */
    void deleteCompilation(Long compId);
}
