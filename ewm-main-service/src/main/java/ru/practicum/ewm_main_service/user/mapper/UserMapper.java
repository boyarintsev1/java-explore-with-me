package ru.practicum.ewm_main_service.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_main_service.user.dto.NewUserRequest;
import ru.practicum.ewm_main_service.user.dto.UserDto;
import ru.practicum.ewm_main_service.user.dto.UserShortDto;
import ru.practicum.ewm_main_service.user.entity.User;

/**
 * Класс UserMapper позволяет преобразовать входящие данные в сущность User, а также преобразовать User
 * в нужный формат возврата данных UserDto.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toUser(NewUserRequest newUserRequest) {
        if (newUserRequest == null) {
            return null;
        }
        return User
                .builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        if (user == null) {
            return null;
        }
        return UserShortDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
