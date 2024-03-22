package ru.practicum.ewm_main_service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс NewCategoryDto содержит информацию c входящего POST/PATCH запроса для добавления/обновления категории.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Значение поля Name не может быть пустым")
    @Size(min = 1, max = 50, message = "Название категории должно быть длиной от 1 до 50 символов")
    private String name;
}

