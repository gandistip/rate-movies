package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    @Builder
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        setNameIfEmpty();
        this.birthday = birthday;
    }

    private void setNameIfEmpty() {
        if (getName() == null || getName().isBlank()) {
            setName(getLogin());
        }
    }
}
