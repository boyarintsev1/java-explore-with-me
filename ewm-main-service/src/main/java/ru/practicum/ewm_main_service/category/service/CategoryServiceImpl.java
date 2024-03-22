package ru.practicum.ewm_main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.category.entity.Category;
import ru.practicum.ewm_main_service.category.repository.CategoryRepository;
import ru.practicum.ewm_main_service.exception.NotFoundException;

/**
 * класс для работы с данными о Category при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Category> findCategories(Integer from, Integer size) {
        log.info("Выполняется запрос на получение категорий");
        Pageable page = PageRequest.of(from, size);
        return categoryRepository.findCategories(page);
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoryById(Long catId) {
        log.info("Выполняется запрос на получение указанной категории");
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна", catId, "Category"));
    }

    @Transactional
    @Override
    public Category createCategory(Category category) {
        log.info("Создан объект : {}", category);
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category updateCategory(Category category, Long catId) {
        Category dbCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна", catId, "Category"));
        dbCategory.setName(category.getName());
        log.info("Обновлен объект : {}", category);
        return categoryRepository.save(dbCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            throw new NotFoundException("Категория не найдена или недоступна", catId, "Category");
        } else {
                categoryRepository.deleteById(catId);
            }
        }
    }


