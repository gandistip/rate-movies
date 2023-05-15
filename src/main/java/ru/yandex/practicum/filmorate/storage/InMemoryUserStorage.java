package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User create(User user) throws ValidationException {
        UserValidationException.validationException(user);
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.debug("ValidationException");
                throw new ValidationException("Пользователь уже есть в базе.");
            }
        }
        user.setId(++userId);
        users.put(userId, user);
        log.info("Получен запрос POST /users");
        return user;
    }

    public User put(User user) throws ValidationException {
        UserValidationException.validationException(user);
        if (!users.containsKey(user.getId())) {
            log.debug("UserNotFoundException");
            throw new UserNotFoundException("Пользователя с таким ID нет в базе.");
        }
        users.put(user.getId(), user);
        log.info("Получен запрос PUT /users");
        return user;
    }

    public User findUserById(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new UserNotFoundException(String.format("Передан null или отрицательный id пользователя"));
        }
        User user = users.get(userId);
        if (user == null) {
            log.debug("UserNotFoundException");
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return user;
    }
}
