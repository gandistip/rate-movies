package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film create(Film film) throws ValidationException;

    Film put(Film film) throws ValidationException;

    Film findFilmById(Integer id);

    User findUserById(Integer userId);
}
