package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getAll();

    User create(User user) throws ValidationException;

    User put(User user) throws ValidationException;

    void addFriend(Integer id, Integer friendId);

    void delFriend(Integer id, Integer friendId);

    void del(Integer id);

    User findUserById(Integer id);

    List<User> getFriends(Integer id);

}
