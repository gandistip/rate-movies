package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) throws ValidationException {
        return userStorage.create(user);
    }

    public User put(User user) throws ValidationException {
        return userStorage.put(user);
    }

    public void addFriend(Integer id, Integer friendId) throws ValidationException {
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
        List<User> friends = new ArrayList<>();
        Set<Integer> friendsId = userStorage.findUserById(id).getFriends();
        for (Integer friendId : friendsId) {
            friends.add(userStorage.findUserById(friendId));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        Set<User> commonFriends = new HashSet<>(getFriends(id));
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

}
