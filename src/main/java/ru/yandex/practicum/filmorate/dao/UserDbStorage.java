package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Primary
@Slf4j
public class UserDbStorage implements ModelStorage<User> {

    public final JdbcTemplate jdbcTemplate;
    public long id = 1;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User object) {
        object.setId(id);
        jdbcTemplate.execute("insert into PUBLIC.Users values(" + getInsertData(object) + ")");
        log.info("Успешно был добавлен объект типа {} с id = {}", object.getClass().getSimpleName(), id);
        id++;
        return object;
    }

    @Override
    public User change(User object) {
        jdbcTemplate.execute("update Users set " + getUpdateData(object) + " where id = " + object.getId());
        return object;
    }

    @Override
    public Optional<User> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from Users where id =" + id);
        if (userRows.next()) {
            var result = getObject(userRows);
            return Optional.of(result);
        } else {
            log.info("Объект с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean remove(long id) {
        jdbcTemplate.execute("delete from Users where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from Users");
    }

    @Override
    public List<User> getAll() {
        var result = new ArrayList<User>();
        var rows = jdbcTemplate.queryForRowSet("select * from Users");
        while (rows.next()) {
            var object = getObject(rows);
            result.add(object);
        }
        return result;
    }

    public List<Long> getFriends(long id) {
        var result = new ArrayList<Long>();
        var rows = jdbcTemplate.queryForRowSet("select * from FRIENDS WHERE user_id = " + id);
        while (rows.next()) {
            var object = rows.getLong("friend_id");
            result.add(object);
        }
        return result;
    }

    public void addFriend(Long id, Long friendId) {
        jdbcTemplate.execute("insert into friends values(" + id + ", " + friendId + ",'')");
    }

    public void removeFriend(Long id, Long friendId) {
        jdbcTemplate.execute("delete from friends where user_id = " + id + " and friend_id = " + friendId);
    }

    @Override
    public User getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var name = set.getString("NAME");
        var email = set.getString("EMAIL");
        var login = set.getString("LOGIN");
        var date = Objects.requireNonNull(set.getDate("BIRTHDAY")).toLocalDate();
        User user = new User(name, email, login, date);
        user.setId(id);
        return user;
    }

    @Override
    public String getInsertData(User object) {
        return "'" +
                object.getId() +
                "'," +
                "'" +
                object.getEmail() +
                "'," +
                "'" +
                object.getLogin() +
                "'," +
                "'" +
                object.getBirthday() +
                "'," +
                "'" +
                object.getName() +
                "'";
    }

    @Override
    public String getUpdateData(User object) {
        return "email = " +
                "'" +
                object.getEmail() +
                "'," +
                "login = " +
                "'" +
                object.getLogin() +
                "'," +
                "birthday = " +
                "'" +
                object.getBirthday() +
                "'," +
                "name = " +
                "'" +
                object.getName() +
                "'";
    }
}
