package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public User create(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос POST /users");
        return userService.create(user);
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос PUT /users");
        return userService.put(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer friendId) throws ValidationException {
        log.info("Получен запрос PUT /users/{id}/friends/{friendId}");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void delFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer friendId) throws ValidationException {
        log.info("Получен запрос DELETE /users/{id}/friends/{friendId}");
        userService.delFriend(id, friendId);
    }

    @GetMapping("/users")
    public Collection<User> getAll() {
        log.info("Получен запрос GET /users");
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET /users/{id}");
        return userService.findUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET /users/{id}/friends");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Integer id,
                                             @PathVariable("otherId") Integer otherId) {
        log.info("Получен запрос GET /users/{id}/friends/common/{otherId}");
        return userService.getCommonFriends(id, otherId);
    }

}
