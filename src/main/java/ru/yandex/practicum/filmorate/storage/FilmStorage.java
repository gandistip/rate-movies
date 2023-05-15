package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film create(@RequestBody Film film) throws ValidationException;

    Film put(@RequestBody Film film) throws ValidationException;

    Film findFilmById(Integer id);

    User findUserById(Integer userId);
}
