package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film put(Film film) throws ValidationException {
        return filmStorage.put(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = filmStorage.findUserById(userId);
        Set<String> likes = film.getLikes();
        likes.add(user.getEmail());
        film.setLikes(likes);
    }

    public void delLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = filmStorage.findUserById(userId);
        Set<String> likes = film.getLikes();
        likes.remove(user.getEmail());
        film.setLikes(likes);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getAll().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

}
