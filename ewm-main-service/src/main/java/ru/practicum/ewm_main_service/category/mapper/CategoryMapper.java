package ru.practicum.ewm_main_service.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main_service.category.dto.CategoryDto;
import ru.practicum.ewm_main_service.category.dto.NewCategoryDto;
import ru.practicum.ewm_main_service.category.entity.Category;

/**
 * Класс CategoryMapper позволяет преобразовать входящие данные в сущность Category, а также преобразовать Category
 * в нужный формат возврата данных CategoryDto.
 */
@UtilityClass
public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto == null) {
            return null;
        }
        return Category
                .builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
