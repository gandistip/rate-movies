package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private User user;
    private User postUser;
    private LocalDate birthday = LocalDate.of(1995, 12, 28);
    private UserController userController;

    @BeforeEach
    public void freshUserController() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void postUser() throws ValidationException {
        user = User.builder().email("почта@").login("кличка").name("имя").birthday(birthday).build();
        postUser = userController.create(user);
        assertEquals(user, postUser, "Ошибка добавления пользователя");
        assertEquals(1, userController.getAll().size(), "Количество пользователей не совпадает");
    }

    @Test
    public void postEmptyUser() {
        user = null;
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void postUserWithoutEmail() {
        user = User.builder().email("").login("кличка").name("имя").birthday(birthday).build();
        assertThrows(ValidationException.class, () -> userController.create(user));

        user = User.builder().email(null).login("кличка").name("имя").birthday(birthday).build();
        assertThrows(ValidationException.class, () -> userController.create(user));

        user = User.builder().login("кличка").name("имя").birthday(birthday).build();
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void postUserWithoutName() throws ValidationException {
        user = User.builder().email("почта@").login("кличка").name("").birthday(birthday).build();
        postUser = userController.create(user);
        assertEquals(user, postUser, "Ошибка добавления пользователя");
        assertEquals(1, userController.getAll().size(), "Количество пользователей не совпадает");

        user = User.builder().email("почта@1").login("кличка").name(null).birthday(birthday).build();
        postUser = userController.create(user);
        assertEquals(user, postUser, "Ошибка добавления пользователя");
        assertEquals(2, userController.getAll().size(), "Количество пользователей не совпадает");

        user = User.builder().email("почта@2").login("кличка").birthday(birthday).build();
        postUser = userController.create(user);
        assertEquals(user, postUser, "Ошибка добавления пользователя");
        assertEquals(3, userController.getAll().size(), "Количество пользователей не совпадает");
    }

}
