package ru.yandex.practicum.filmorate.storages;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.models.Model;

import java.util.List;
import java.util.Optional;

public interface ModelStorage<T extends Model> {
    T add(T object);

    Optional<T> find(long id);

    boolean remove(long id);

    void clear();

    List<T> getAll();

    T change(T model);

    String getUpdateData(T object);

    String getInsertData(T object);

    T getObject(SqlRowSet set);
}
