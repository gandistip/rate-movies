package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
@Slf4j
public class UserException {
    public static void validationException(@RequestBody User user) throws ValidationException {

        if (user == null) {
            log.debug("ValidationException");
            throw new ValidationException("Запрос не может быть пустым.");
        }

        if(user.getEmail() == null || user.getEmail().isBlank()) {
            log.debug("ValidationException");
            throw new ValidationException("Email не может быть пустым.");
        }

        if(!user.getEmail().contains("@")) {
            log.debug("ValidationException");
            throw new ValidationException("Email не может быть без @.");
        }

        if(user.getLogin() == null || user.getLogin().isBlank()) {
            log.debug("ValidationException");
            throw new ValidationException("Логин не может быть пустым.");
        }

        if(user.getLogin().contains(" ")) {
            log.debug("ValidationException");
            throw new ValidationException("Логин не может содержать пробел.");
        }

        try {
            if(user.getBirthday().isAfter(LocalDate.now())) {
                log.debug("ValidationException");
                throw new ValidationException("Дата рождения не может быть в будущем.");
            }
        } catch (NullPointerException e) {}

    }
}
