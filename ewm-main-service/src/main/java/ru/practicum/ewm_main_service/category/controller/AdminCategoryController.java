package ru.practicum.ewm_main_service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;
import ru.practicum.ewm_main_service.category.entity.Category;
import ru.practicum.ewm_main_service.category.mapper.CategoryMapper;
import ru.practicum.ewm_main_service.category.service.CategoryService;

import javax.validation.Valid;

/**
 * Класс-контроллер по Category для пользователя Admin
 */
@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {
    private final CategoryService categoryService;

    /**
     * метод создания новой категории
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        log.info("Создание запроса на добавление категории...");
        Category newCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(CategoryMapper.toCategoryDto(newCategory),
                HttpStatus.CREATED);
    }

    /**
     * метод обновления данных для категории с указанным ID
     */
    @PatchMapping(path = "/{catId}", headers = "Accept=application/json")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("catId") Long catId,
                                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        log.info("Создание запроса на обновление категории...");
        Category updatedCategory = categoryService.updateCategory(category, catId);
        return new ResponseEntity<>(CategoryMapper.toCategoryDto(updatedCategory),
                HttpStatus.OK);
    }

    /**
     * метод удаления категории
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("catId") Long catId) {
        categoryService.deleteCategory(catId);
        log.info("Создание запроса на удаление категории...");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

