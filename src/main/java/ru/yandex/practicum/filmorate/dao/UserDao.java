package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("UserDao")
@Primary
public class UserDao extends ModelDao<User>{
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "USERS");
    }

    public List<Long> getFriends(long id) {
        var result = new ArrayList<Long>();
        var rows = jdbcTemplate.queryForRowSet("select * from FRIENDS WHERE user_id = " + id );
        while (rows.next()) {
            var object = rows.getLong("friend_id");
            result.add(object);
        }
        return result;
    }

    public void addFriend(Long id, Long friendId) {
        jdbcTemplate.execute("insert into friends values(" + id + ", " + friendId + ",'')" );
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
        var date = set.getDate("BIRTHDAY").toLocalDate();
        User user = new User(name, email, login, date);
        user.setId(id);
        return user;
    }

    @Override
    public String getInsertData(User object) {
        StringBuilder builder = new StringBuilder();
        builder.append("'").
                append(object.getId()).
                append("',").
                append("'").
                append(object.getEmail()).
                append("',").
                append("'").
                append(object.getLogin()).
                append("',").
                append("'").
                append(object.getBirthday()).
                append("',").
                append("'").
                append(object.getName()).
                append("'");
        return builder.toString();
    }

    @Override
    public String getUpdateData(User object) {
        StringBuilder builder = new StringBuilder();
        builder.append("email = ").
                append("'").
                append(object.getEmail()).
                append("',").
                append("login = ").
                append("'").
                append(object.getLogin()).
                append("',").
                append("birthday = ").
                append("'").
                append(object.getBirthday()).
                append("',").
                append("name = ").
                append("'").
                append(object.getName()).
                append("'");
        return builder.toString();
    }
}
