package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User create(@RequestBody User user) throws ValidationException;

    User put(@RequestBody User user) throws ValidationException;

    User findUserById(Integer id);
}
