package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {
        log.info("Получение всех жанров");
        return genreStorage.getAll();
    }

    public Genre findGenreById(Integer id) {
        log.info("Получение жанра c id: {}", id);
        if (id == null || id <= 0) {
            throw new NotFoundException(String.format("Передан null или отрицательный id"));
        }
        return genreStorage.findGenreById(id);
    }

}
