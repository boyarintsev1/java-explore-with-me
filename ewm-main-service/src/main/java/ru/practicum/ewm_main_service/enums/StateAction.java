package ru.practicum.ewm_main_service.enums;

/**
 * StateAction - новое состояние события Event при обновлении администратором. Может принимать одно из следующих
 * значений:
 *     1. PUBLISH_EVENT - подтверждение публикации события (admin).
 *     2. REJECT_EVENT - отклонить публикацию события (admin).
 *     3. SEND_TO_REVIEW - отправить на подтверждение (user).
 *     4. CANCEL_REVIEW - отменить заявку (user).
 */
public enum StateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}
