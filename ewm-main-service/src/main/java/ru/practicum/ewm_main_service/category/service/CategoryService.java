package ru.practicum.ewm_main_service.category.service;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main_service.category.entity.Category;

/**
 * интерфейс для работы с данными о Category
 */
public interface CategoryService {

    /**
     * метод получения информации о категориях
     */
    Page<Category> findCategories(Integer from, Integer size);

    /**
     * метод получения информации о категории по ID
     */
    Category findCategoryById(Long catId);

    /**
     * метод создания новой категории
     */
    Category createCategory(Category category);

    /**
     * метод обновления данных для категории с указанным ID
     */
    Category updateCategory(Category category, Long catId);

    /**
     * метод удаления категории
     */
    void deleteCategory(Long catId);
}
