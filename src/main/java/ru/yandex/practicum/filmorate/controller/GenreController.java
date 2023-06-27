package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public Collection<Genre> getAll() {
        log.info("Получен запрос GET /genres");
        return genreService.getAll();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET /genres/{id}");
        return genreService.findGenreById(id);
    }

}
