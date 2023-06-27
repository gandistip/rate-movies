package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (Exception e) {
            log.debug("NotFoundException");
            throw new NotFoundException("Такого ID нет в базе.");
        }
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

}
