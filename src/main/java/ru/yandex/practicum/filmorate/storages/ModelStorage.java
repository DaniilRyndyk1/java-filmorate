package ru.yandex.practicum.filmorate.storages;

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
}
