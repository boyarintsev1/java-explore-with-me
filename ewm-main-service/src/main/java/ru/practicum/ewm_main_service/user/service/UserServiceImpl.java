package ru.practicum.ewm_main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main_service.exception.NotFoundException;
import ru.practicum.ewm_main_service.user.entity.User;
import ru.practicum.ewm_main_service.user.repository.UserRepository;

/**
 * класс для работы с данными о пользователе User при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<User> findUsers(Integer[] ids, Integer from, Integer size) {
        log.info("Выполняется запрос на получение всех пользователей");
        Pageable page = PageRequest.of(from, size);
        return ids == null
                ? userRepository.findAllUsers(page)
                : userRepository.findIdsUsers(ids, page);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long userId) {
        log.info("Выполняется запрос на получение пользователя UserShortDto");
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден или недоступен", userId, "User"));
    }

    @Transactional
    @Override
    public User createUser(User user) {
        log.info("Создан объект : {}", user);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден или недоступен", userId, "User");
        } else {
            userRepository.deleteById(userId);
        }
    }
}

