package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int count;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmException.validationException(film);
        for (Film f: films.values()) {
            if (f.equals(film)) {
                log.debug("ValidationException");
                throw new ValidationException("Фильм уже есть в базе.");
            }
        }
        film.setId(++count);
        films.put(count, film);
        log.info("Получен запрос POST /films");
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        FilmException.validationException(film);
        if (!films.containsKey(film.getId())) {
            log.debug("ValidationException");
            throw new ValidationException("Фильма с таким ID нет в базе.");
        }
        films.put(film.getId(), film);
        log.info("Получен запрос PUT /films");
        return film;
    }

}
