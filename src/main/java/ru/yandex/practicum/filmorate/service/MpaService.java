package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAll() {
        log.info("Получение всех MPA");
        return mpaStorage.getAll();
    }

    public Mpa findMpaById(Integer id) {
        log.info("Получение MPA c id: {}", id);
        if (id == null || id <= 0) {
            throw new NotFoundException(String.format("Передан null или отрицательный id"));
        }
        return mpaStorage.findMpaById(id);
    }

}
