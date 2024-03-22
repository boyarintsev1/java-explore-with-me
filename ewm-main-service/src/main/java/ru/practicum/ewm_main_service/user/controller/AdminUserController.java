package ru.practicum.ewm_main_service.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main_service.user.dto.NewUserRequest;
import ru.practicum.ewm_main_service.user.dto.UserDto;
import ru.practicum.ewm_main_service.user.entity.User;
import ru.practicum.ewm_main_service.user.mapper.UserMapper;
import ru.practicum.ewm_main_service.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс-контроллер по User
 */
@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * метод получения информации о пользователях
     */
    @GetMapping(headers = "Accept=application/json")
    public ResponseEntity<List<UserDto>> findUsers(
            @RequestParam(value = "ids", required = false) Integer[] ids,
            @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Исполняется запрос на получение информации о пользователях.");
        return userService.findUsers(ids, from, size).isEmpty()
                ? new ResponseEntity<>(List.of(), HttpStatus.OK)
                : new ResponseEntity<>(userService.findUsers(ids, from, size)
                .map(userMapper::toUserDto)
                .getContent(), HttpStatus.OK);
    }

    /**
     * метод создания нового пользователя
     */
    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        User user = userMapper.toUser(newUserRequest);
        log.info("Пользователь зарегистрирован");
        return new ResponseEntity<>(userMapper.toUserDto(userService.createUser(user)), HttpStatus.CREATED);
    }

    /**
     * метод удаления пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь удален");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

