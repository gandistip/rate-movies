package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User create(User user) throws ValidationException {
        String sqlCreate = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlCreate,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        String sqlGet = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, user.getEmail());
    }

    @Override
    public User put(User user) {
        String sqlUpdate = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlUpdate,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId()
        );
        return findUserById(user.getId());
    }

    @Override
    public void del(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User findUserById(Integer id) {
        String sqlGet = "SELECT * FROM users WHERE id = ?";
        String sqlFriends = "SELECT * FROM friend WHERE user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id);
            assert user != null;
            try {
                user.setFriends(new HashSet<>(jdbcTemplate.query(sqlFriends, this::mapRowToFriendSet, id)));
            } catch (Exception e) {
                return user;
            }
            return user;
        } catch (Exception e) {
            log.debug("UserNotFoundException");
            throw new UserNotFoundException("Пользователя с таким ID нет в базе.");
        }
    }

    @Override
    public List<User> getFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        Set<Integer> friendsId = findUserById(id).getFriends();
        for (Integer friendId : friendsId) {
            friends.add(findUserById(friendId));
        }
        return friends;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sql = "INSERT INTO friend (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void delFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        user.setId(rs.getInt("id"));
        return user;
    }

    private Integer mapRowToFriendSet(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("friend_id");
    }

}
