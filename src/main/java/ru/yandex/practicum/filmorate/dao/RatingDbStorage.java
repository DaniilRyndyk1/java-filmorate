package ru.yandex.practicum.filmorate.dao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@Slf4j
public class RatingDbStorage implements ModelStorage<Rating> {

    public final JdbcTemplate jdbcTemplate;
    public long id = 1;

    @Autowired
    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating add(Rating object) {
        object.setId(id);
        jdbcTemplate.execute("insert into PUBLIC.Rating values(" + getInsertData(object) + ")");
        log.info("Успешно был добавлен объект типа {} с id = {}", object.getClass().getSimpleName(), id);
        id++;
        return object;
    }

    @Override
    public Rating change(Rating object) {
        jdbcTemplate.execute("update Rating set " + getUpdateData(object) + " where id = " + object.getId());
        return object;
    }

    @Override
    public Optional<Rating> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from Rating where id =" + id);
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
        jdbcTemplate.execute("delete from Rating where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from Rating");
    }

    @Override
    public List<Rating> getAll() {
        var result = new ArrayList<Rating>();
        var rows = jdbcTemplate.queryForRowSet("select * from Rating");
        while (rows.next()) {
            var object = getObject(rows);
            result.add(object);
        }
        return result;
    }

    @Override
    public Rating getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var name = set.getString("NAME");
        return new Rating(id, name);
    }

    @Override
    public String getInsertData(Rating object) {
        return "'" +
                object.getId() +
                "'," +
                "'" +
                object.getName() +
                "'";
    }

    @Override
    public String getUpdateData(Rating object) {
        return "name = " +
                "'" +
                object.getName() +
                "'";
    }
}