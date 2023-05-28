package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.models.Model;
import ru.yandex.practicum.filmorate.storages.ModelStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ModelDao<T extends Model> implements ModelStorage<T> {
    public final JdbcTemplate jdbcTemplate;
    public final String tableName;
    public long id = 1;

    public ModelDao(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
    }

    public T getObject(SqlRowSet set) {
        return null;
    }

    public String getInsertData(T object) {
        return null;
    }

    public String getUpdateData(T object) {
        return null;
    }

    @Override
    public T add(T object) {
        object.setId(id);
        jdbcTemplate.execute("insert into PUBLIC." + tableName + " values("+ getInsertData(object) + ")");
        log.info("Успешно был добавлен объект типа {} с id = {}", object.getClass().getSimpleName(), id);
        id++;
        return object;
    }

    @Override
    public Optional<T> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from " + tableName + " where id =" + id);
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
        jdbcTemplate.execute("delete from " + tableName + " where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from " + tableName);
    }

    @Override
    public List<T> getAll() {
        var result = new ArrayList<T>();
        var rows = jdbcTemplate.queryForRowSet("select * from " + tableName);
        while (rows.next()) {
            var object = getObject(rows);
            result.add(object);
        }
        return result;
    }

    @Override
    public T change(T model) {
        jdbcTemplate.execute("update " + tableName + " set " + getUpdateData(model) + " where id = " + model.getId());
        return model;
    }
}
