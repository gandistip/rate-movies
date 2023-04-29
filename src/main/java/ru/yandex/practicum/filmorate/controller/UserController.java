package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int count;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        UserException.validationException(user);
        for (User u: users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.debug("ValidationException");
                throw new ValidationException("Пользователь уже есть в базе.");
            }
        }
        user.setId(++count);
        users.put(count, user);
        log.info("Получен запрос POST /users");
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        UserException.validationException(user);
        if (!users.containsKey(user.getId())) {
            log.debug("ValidationException");
            throw new ValidationException("Пользователя с таким ID нет в базе.");
        }
        users.put(user.getId(), user);
        log.info("Получен запрос PUT /users");
        return user;
    }

}
