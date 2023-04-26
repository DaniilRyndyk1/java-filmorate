package ru.yandex.practicum.filmorate.managers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class InMemoryModelManager<T extends Model> implements ModelManager<T> {
    private final HashMap<Integer, T> objects = new HashMap<>();
    private Integer id = 1;

    @Override
    public T add(T object) {
        object.setId(id);
        objects.put(id, object);
        log.info("Успешно был добавлен объект типа " + object.getClass().getSimpleName() + " с id = " + id);
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
    public ArrayList<T> getAll() {
        ArrayList<T> result = new ArrayList<>();
        for (int id : objects.keySet()) {
            var value = objects.get(id);
            result.add(value);
        }
        return result;
    }

    @Override
    public T change(T model) {
        int id = model.getId();
        remove(id);
        objects.put(id, model);
        log.info("Успешно был обновлен объект типа " + model.getClass().getSimpleName() + " с id = " + id);
        return model;
    }
}
