package ru.yandex.practicum.filmorate.managers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.models.Model;

import java.util.*;

@Slf4j
public class InMemoryModelManager<T extends Model> implements ModelManager<T> {
    private final Map<Integer, T> objects;
    private Integer id = 1;

    public InMemoryModelManager() {
        objects = new HashMap<>();
    }

    @Override
    public T add(T object) {
        object.setId(id);
        objects.put(id, object);
        log.info("Успешно был добавлен объект типа {} с id = {}", object.getClass().getSimpleName(), id);
        id++;
        return object;
    }

    @Override
    public Optional<T> find(int id) {
        return Optional.ofNullable(objects.get(id));
    }

    @Override
    public boolean remove(int id) {
        if (objects.containsKey(id)) {
            objects.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(objects.values());
    }

    @Override
    public T change(T model) {
        int id = model.getId();
        remove(id);
        objects.put(id, model);
        log.info("Успешно был обновлен объект типа {} с id = {}", model.getClass().getSimpleName(), id);
        return model;
    }
}
