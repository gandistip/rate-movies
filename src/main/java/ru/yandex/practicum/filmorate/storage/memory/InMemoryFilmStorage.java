package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidationException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private Integer filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        FilmValidationException.validationException(film);
        film.setId(++filmId);
        if (films.containsKey(film.getId())) {
            log.debug("ValidationException");
            throw new ValidationException("Фильм уже есть в базе.");
        }
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film put(Film film) throws ValidationException {
        FilmValidationException.validationException(film);
        if (!films.containsKey(film.getId())) {
            log.debug("FilmNotFoundException");
            throw new FilmNotFoundException("Фильма с таким id нет в базе.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void del(Integer id) {

    }

    @Override
    public Film findFilmById(Integer filmId) {
        if (filmId == null || filmId <= 0) {
            throw new FilmNotFoundException(String.format("Передан null или отрицательный id фильма"));
        }
        Film film = films.get(filmId);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void delLike(Integer filmId, Integer userId) {

    }

    @Override
    public List<Integer> getLikes(int filmId) {
        return null;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return null;
    }

}
