package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) throws ValidationException {
        user.setId(++userId);
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.debug("ValidationException");
                throw new ValidationException("Пользователь уже есть в базе.");
            }
        }
        users.put(userId, user);
        return user;
    }

    @Override
    public User put(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("UserNotFoundException");
            throw new UserNotFoundException("Пользователя с таким ID нет в базе.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
    }

    @Override
    public void delFriend(Integer id, Integer friendId) {
    }

    @Override
    public void del(Integer id) {

    }

    @Override
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

    @Override
    public List<User> getFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        Set<Integer> friendsId = findUserById(id).getFriends();
        for (Integer friendId : friendsId) {
            friends.add(findUserById(friendId));
        }
        return friends;
    }
}
