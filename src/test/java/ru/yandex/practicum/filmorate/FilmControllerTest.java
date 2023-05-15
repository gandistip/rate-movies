package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private Film film;
    private Film postFilm;
    private LocalDate releaseDate = LocalDate.of(1995, 12, 28);
    private FilmController filmController;

    @BeforeEach
    public void newFilmAndController() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(new InMemoryUserStorage())));
    }

    @Test
    public void postFilm() throws ValidationException {
        film = Film.builder().name("название").description("описание").releaseDate(releaseDate).duration(99).build();
        postFilm = filmController.create(film);
        assertEquals(film, postFilm, "Ошибка добавления фильма");
        assertEquals(1, filmController.getAll().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postEmptyFilm() {
        film = null;
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void postFilmWithoutName() {
        film = Film.builder().name("").description("описание").releaseDate(releaseDate).duration(99).build();
        assertThrows(ValidationException.class, () -> filmController.create(film));

        film = Film.builder().name(null).description("описание").releaseDate(releaseDate).duration(99).build();
        assertThrows(ValidationException.class, () -> filmController.create(film));

        film = Film.builder().description("описание").releaseDate(releaseDate).duration(99).build();
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void postFilmWithoutDescription() throws ValidationException {
        film = Film.builder().name("название").description("").releaseDate(releaseDate).duration(99).build();
        postFilm = filmController.create(film);
        assertEquals(film, postFilm, "Ошибка добавления фильма");
        assertEquals(1, filmController.getAll().size(), "Количество фильмов не совпадает");

        film = Film.builder().name("название2").description(null).releaseDate(releaseDate).duration(99).build();
        postFilm = filmController.create(film);
        assertEquals(film, postFilm, "Ошибка добавления фильма");
        assertEquals(2, filmController.getAll().size(), "Количество фильмов не совпадает");

        film = Film.builder().name("название3").releaseDate(releaseDate).duration(99).build();
        postFilm = filmController.create(film);
        assertEquals(film, postFilm, "Ошибка добавления фильма");
        assertEquals(3, filmController.getAll().size(), "Количество фильмов не совпадает");
    }

}
