package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/mpa")
    public Collection<Mpa> getAll() {
        log.info("Получен запрос GET /mpa");
        return mpaService.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET /mpa/{id}");
        return mpaService.findMpaById(id);
    }

}
