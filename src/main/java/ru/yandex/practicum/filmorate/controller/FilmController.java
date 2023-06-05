package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос POST /films");
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film put(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос PUT /films");
        return filmService.put(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id,
                        @PathVariable("userId") Integer userId) {
        log.info("Получен запрос PUT /films/{id}/like/{userId}");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void delLike(@PathVariable("id") Integer id,
                        @PathVariable("userId") Integer userId) {
        log.info("Получен запрос DELETE /films/{id}/like/{userId}");
        filmService.delLike(id, userId);
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        log.info("Получен запрос GET /films");
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET /films/{id}");
        return filmService.findFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) throws IncorrectParameterException {
        log.info("Получен запрос GET /films/popular");
        return filmService.getTopFilms(count);
    }

}
