package ru.yandex.practicum.filmorate.managers;

import ru.yandex.practicum.filmorate.models.Model;

import java.util.ArrayList;
import java.util.Optional;

public interface ModelManager<T extends Model> {
    T add(T object);
    Optional<T> find(int id);
    boolean remove(int id);
    void clear();
    ArrayList<T> getAll();
    T change(T model);
}
