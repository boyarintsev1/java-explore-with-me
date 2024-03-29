package ru.practicum.ewm_main_service.enums;

/**
 * Status - статус заявки на участие в событии:
 * 1. PENDING - ожидание подтверждения админом.
 * 2. CONFIRMED - подтверждена. В это состояние событие переводит администратор.
 * 3. REJECTED - отмена. В это состояние событие переходит в двух случаях.
 *    Первый — по решению администратора.
 *    Второй — когда инициатор заявки решил отменить её на этапе ожидания подтверждения.
 */
public enum Status {
    PENDING,
    CANCELED,
    CONFIRMED,
    REJECTED
}

