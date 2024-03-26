package ru.practicum.ewm_main_service.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.compilation.entity.Compilation;


@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "SELECT c.* FROM public.compilations c WHERE c.pinned = ?1 ORDER BY c.id",
            nativeQuery = true)
    Page<Compilation> findPinnedCompilations(Boolean pinned, Pageable page);

    @Query(value = "SELECT c.* FROM public.compilations c ORDER BY c.id",
            nativeQuery = true)
    Page<Compilation> findAllCompilations(Pageable page);
}

