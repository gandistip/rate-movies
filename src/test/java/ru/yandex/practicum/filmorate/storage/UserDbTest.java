package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbTest {
    private final UserStorage userStorage;
    private User user;
    private User friend;

    @BeforeEach
    public void createUser() throws ValidationException {

        user = User.builder()
                .email("email@")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user.setId(1);
        userStorage.create(user);

        friend = User.builder()
                .email("email@friend")
                .login("loginFriend")
                .name("nameFriend")
                .birthday(LocalDate.of(2000, 1, 2))
                .build();
        friend.setId(2);
        userStorage.create(friend);
    }

    @Test
    void addUser() {
        assertEquals(user, userStorage.findUserById(user.getId()));
    }

    @Test
    void putUser() throws ValidationException {
        User upUser = User.builder()
                .email("upEmail@")
                .login("upLogin")
                .name("upName")
                .birthday(LocalDate.of(2003, 3, 3))
                .build();
        upUser.setId(1);
        assertEquals(upUser, userStorage.put(upUser));
    }

    @Test
    void deleteUser() {
        assertEquals(user, userStorage.findUserById(user.getId()));
        userStorage.del(user.getId());
        assertThrows(UserNotFoundException.class, () -> userStorage.findUserById(user.getId()));
    }

    @Test
    void getAllUser() {
        assertArrayEquals(List.of(user, friend).toArray(), userStorage.getAll().toArray());
    }

    @Test
    void getUser() {
        assertEquals(user, userStorage.findUserById(user.getId()));
    }

    @Test
    void addFriend() {
        userStorage.addFriend(1, 2);
        assertEquals(List.of(userStorage.findUserById(2)), userStorage.getFriends(1));
    }

    @Test
    void deleteFriend() {
        userStorage.addFriend(1, 2);
        userStorage.delFriend(1, 2);
        assertArrayEquals(Collections.emptyList().toArray(), userStorage.getFriends(1).toArray());
    }

}