package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbTest {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Film film;
    private Film film2;
    private User user;
    private User user2;

    @BeforeEach
    public void createFilm() throws ValidationException {

        film = Film.builder()
                .name("film")
                .description("d")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .build();
        film.setId(1);
        filmStorage.create(film);

        film2 = Film.builder()
                .name("film2")
                .description("d2")
                .releaseDate(LocalDate.of(2000, 1, 2))
                .duration(102)
                .mpa(new Mpa(2, "PG"))
                .build();
        film2.setId(2);
        filmStorage.create(film2);

        user = User.builder()
                .email("email@")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user.setId(1);
        userStorage.create(user);

        user2 = User.builder()
                .email("email@2")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, 1, 2))
                .build();
        user2.setId(2);
        userStorage.create(user2);

    }

    @Test
    void addFilm() {
        assertEquals(film, filmStorage.findFilmById(film.getId()));
    }

    @Test
    void putFilm() throws ValidationException {
        Film updatedFilm = Film.builder()
                .name("upFilm")
                .description("upD")
                .releaseDate(LocalDate.of(2003, 3, 3))
                .duration(103)
                .mpa(new Mpa(3, "PG-13"))
                .build();
        updatedFilm.setId(film.getId());
        assertEquals(updatedFilm, filmStorage.put(updatedFilm));
    }

    @Test
    void deleteFilm() {
        film.setMpa(new Mpa(1, "G"));
        assertEquals(film, filmStorage.findFilmById(film.getId()));
        filmStorage.del(film.getId());
        assertThrows(FilmNotFoundException.class, () -> filmStorage.findFilmById(film.getId()));
    }

    @Test
    void getFilm() {
        assertEquals(film, filmStorage.findFilmById(film.getId()));
    }

    @Test
    void getAllFilm() {
        assertArrayEquals(List.of(film, film2).toArray(), filmStorage.getAll().toArray());
    }

    @Test
    void addLike() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        assertEquals(filmStorage.getLikes(1).size(), 2);
    }

    @Test
    void deleteLike() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        filmStorage.delLike(1, 1);
        assertEquals(filmStorage.getLikes(1).size(), 1);
    }

    @Test
    public void getTopFilms() {
        filmStorage.addLike(1, 1);
        filmStorage.addLike(2, 1);
        List<Film> popularFilm = filmStorage.getTopFilms(2);
        assertEquals(popularFilm.size(), 2);
        assertEquals("film", popularFilm.get(0).getName());
    }

}