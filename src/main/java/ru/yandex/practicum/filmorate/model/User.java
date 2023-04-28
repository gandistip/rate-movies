package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @Builder
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        setNameIfEmpty();
        this.birthday = birthday;
    }

    private void setNameIfEmpty() {
        if(getName() == null || getName().isBlank()) {
             setName(getLogin());
        }
    }
}
