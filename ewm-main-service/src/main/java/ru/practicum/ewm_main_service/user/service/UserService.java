package ru.practicum.ewm_main_service.user.service;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main_service.user.entity.User;

/**
 * интерфейс для работы с данными о User
 */
public interface UserService {

    /**
     * метод получения информации о пользователях
     */
    Page<User> findUsers(Integer[] ids, Integer from, Integer size);

    /**
     * метод получения краткой информации о пользователе по его Id
     */
    User findUserById(Long userId);

    /**
     * метод создания нового пользователя
     */
    User createUser(User user);

    /**
     * метод удаления данных о пользователе
     */
    void deleteUser(Long userId);
}
