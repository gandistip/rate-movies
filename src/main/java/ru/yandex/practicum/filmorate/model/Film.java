package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
