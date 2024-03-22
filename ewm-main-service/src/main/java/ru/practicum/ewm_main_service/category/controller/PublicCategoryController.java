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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс-контроллер по Category (публичный)
 */
@RestController
@Slf4j
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * метод получения информации о категориях
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<CategoryDto>> findCategories(
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Категории найдены");
        return categoryService.findCategories(from, size).isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(categoryService.findCategories(from, size)
                .map(categoryMapper::toCategoryDto)
                .getContent(), HttpStatus.OK);
    }

    /**
     * метод получения информации о категории по ID
     */
    @GetMapping(path = "/{catId}", headers = "Accept=application/json")
    public ResponseEntity<CategoryDto> findCategories(@PathVariable("catId") Long catId) {
        log.info("Категория найдена");
        return new ResponseEntity<>(categoryMapper.toCategoryDto(categoryService.findCategoryById(catId)),
                HttpStatus.OK);
    }

    /**
     * метод создания новой категории
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        log.info("Категория добавлена");
        return new ResponseEntity<>(categoryMapper.toCategoryDto(categoryService.createCategory(category)),
                HttpStatus.CREATED);
    }

    /**
     * метод обновления данных для категории с указанным ID
     */
    @PatchMapping(path = "/{catId}", headers = "Accept=application/json")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("catId") Long catId,
                                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        log.info("Данные категории изменены");
        return new ResponseEntity<>(categoryMapper.toCategoryDto(categoryService.updateCategory(category, catId)),
                HttpStatus.OK);
    }

    /**
     * метод удаления категории
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("catId") Long catId) {
        categoryService.deleteCategory(catId);
        log.info("Категория удалена");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

