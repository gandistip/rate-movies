package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> getAll() {
        log.info("Получение всех фильмов");
        return filmStorage.getAll();
    }

    public Film create(Film film) throws ValidationException {
        log.info("Сохранение фильма: {}", film);
        FilmValidationException.validationException(film);
        return filmStorage.create(film);
    }

    public Film put(Film film) throws ValidationException {
        log.info("Обновление фильма: {}", film);
        FilmValidationException.validationException(film);
        return filmStorage.put(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        log.info("Добавление лайка пользователем: {}, к фильму {}", userId, filmId);
        if (userId == null || userId <= 0 || filmId == null || filmId <= 0) {
            throw new UserNotFoundException(String.format("Передан null или отрицательный id"));
        }
        filmStorage.addLike(filmId, userId);
    }

    public void delLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка пользователем: {}, к фильму {}", userId, filmId);
        if (userId == null || userId <= 0 || filmId == null || filmId <= 0) {
            throw new UserNotFoundException(String.format("Передан null или отрицательный id"));
        }
        filmStorage.delLike(filmId, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        log.info("Получение топа фильмов");
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmStorage.getAll().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer id) {
        log.info("Получение фильма c id: {}", id);
        return filmStorage.findFilmById(id);
    }

}
