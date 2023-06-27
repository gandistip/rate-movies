package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.getAll();
    }

    public User create(User user) throws ValidationException {
        log.info("Создание пользователя: {}", user);
        UserValidationException.validationException(user);
        return userStorage.create(user);
    }

    public User put(User user) throws ValidationException {
        log.info("Обновление пользователя: {}", user);
        UserValidationException.validationException(user);
        return userStorage.put(user);
    }

    public void addFriend(Integer id, Integer friendId) throws ValidationException {
        log.info("Добавление друга: {} пользователем: {}", friendId, id);
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new UserNotFoundException(String.format("Передан null или отрицательный id пользователя"));
        }
        userStorage.addFriend(id, friendId);
    }

    public void delFriend(Integer id, Integer friendId) throws ValidationException {
        log.info("Удаление друга: {} пользователем: {}", friendId, id);
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new UserNotFoundException(String.format("Передан null или отрицательный id пользователя"));
        }
        userStorage.delFriend(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        log.info("Получение списка друзей пользователя: {}", id);
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        log.info("Получение списка общих друзей для: {} и {}", id, otherId);
        Set<User> commonFriends = new HashSet<>(getFriends(id));
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }

    public User findUserById(Integer id) {
        log.info("Получение пользователя c id: {}", id);
        return userStorage.findUserById(id);
    }

}
