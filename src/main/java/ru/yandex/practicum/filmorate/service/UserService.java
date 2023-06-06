package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

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
        return userStorage.create(user);
    }

    public User put(User user) throws ValidationException {
        log.info("Обновление пользователя: {}", user);
        return userStorage.put(user);
    }

    public void addFriend(Integer id, Integer friendId) throws ValidationException {
        log.info("Добавление друга: {} пользователем: {}", friendId, id);
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);

        Set<Integer> userFriends = user.getFriends();
        userFriends.add(friendId);
        user.setFriends(userFriends);
        userStorage.put(user);

        Set<Integer> friendUsers = friend.getFriends();
        friendUsers.add(id);
        friend.setFriends(friendUsers);
        userStorage.put(friend);
    }

    public void delFriend(Integer id, Integer friendId) throws ValidationException {
        log.info("Удаление друга: {} пользователем: {}", friendId, id);
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);

        Set<Integer> userFriends = user.getFriends();
        userFriends.remove(friendId);
        user.setFriends(userFriends);
        userStorage.put(user);

        Set<Integer> friendUsers = friend.getFriends();
        friendUsers.remove(id);
        friend.setFriends(friendUsers);
        userStorage.put(friend);
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
