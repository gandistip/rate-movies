package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getAll();

    Film create(Film film) throws ValidationException;

    Film put(Film film) throws ValidationException;

    void del(Integer id);

    Film findFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

    void delLike(Integer filmId, Integer userId);

    List<Integer> getLikes(int filmId);

    List<Film> getTopFilms(Integer count);
}
