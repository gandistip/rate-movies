package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name mpa_name " +
                "FROM film f JOIN mpa m ON f.mpa_id=m.id";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) throws ValidationException {
        String sqlCreate = "INSERT INTO film" +
                "(name, description, release_date, duration, mpa_id)" +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, film.getName());
            pst.setString(2, film.getDescription());
            pst.setDate(3, Date.valueOf(film.getReleaseDate()));
            pst.setInt(4, film.getDuration());
            pst.setInt(5, film.getMpa().getId());
            return pst;
        }, keyHolder);
        Integer id = keyHolder.getKey().intValue();
        for (Genre genre : film.getGenres()) {
            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlGenre, id, genre.getId());
        }
        return findFilmById(id);
    }

    @Override
    public Film put(Film film) throws ValidationException {
        String sqlUpdate = "UPDATE film SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlUpdate,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(),
                film.getId()
        );
        String sqlDelGenre = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDelGenre, film.getId());
        for (Genre genre : film.getGenres()) {
            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
        }
        return findFilmById(film.getId());
    }

    @Override
    public void del(Integer id) {
        String sql = "DELETE FROM film WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film findFilmById(Integer id) {
        String sqlGet = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "m.id mpa_id, m.name mpa_name " +
                "FROM film f " +
                "JOIN mpa m ON f.mpa_id=m.id " +
                "WHERE f.id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlGet, this::mapRowToFilm, id);
            return film;
        } catch (Exception e) {
            log.debug("FilmNotFoundException");
            throw new FilmNotFoundException("Фильма с таким ID нет в базе.");
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_like (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void delLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_like WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public List<Integer> getLikes(int filmId) {
        String sql = "SELECT user_id FROM film_like WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        final String qs = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "m.id mpa_id, m.name mpa_name " +
                "FROM film f " +
                "JOIN mpa m ON m.id = f.mpa_id " +
                "JOIN film_like fl ON f.id = fl.film_id " +
                "GROUP BY f.id " +
                "ORDER BY f.id " +
                "LIMIT ?";
        return jdbcTemplate.query(qs, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .build();
        film.setId(rs.getInt("id"));
        String sqlGenre = "SELECT * FROM film_genre fg JOIN genre g ON fg.genre_id=g.id WHERE film_id = ?";
        String sqlLike = "SELECT * FROM film_like WHERE film_id = ?";
        film.setGenres(new HashSet<>(jdbcTemplate.query(sqlGenre, this::mapRowToGenre, film.getId())));
        film.setLikes(jdbcTemplate.query(sqlLike, this::mapRowToLike, film.getId()));
        return film;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
        return genre;
    }

    private Integer mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }
}
