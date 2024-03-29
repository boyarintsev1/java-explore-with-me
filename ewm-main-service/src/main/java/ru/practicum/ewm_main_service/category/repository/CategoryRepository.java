package ru.practicum.ewm_main_service.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_main_service.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c.id, c.name FROM public.categories c ORDER BY c.id",
            nativeQuery = true)
    Page<Category> findCategories(Pageable page);
}

