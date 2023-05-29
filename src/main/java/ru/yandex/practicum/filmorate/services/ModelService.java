package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Model;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class ModelService<T extends Model> {
    private final ModelStorage<T> manager;

    public T get(@PathVariable long id) {
        var object = manager.find(id);
        if (object.isEmpty()) {
            throw new NotFoundException(id, object.getClass().getSimpleName());
        }
        return object.get();
    }

    public List<T> getAll() {
        return manager.getAll();
    }

    public void clear() {
        manager.clear();
    }

    public T create(@RequestBody T object) {
        validate(object);
        return manager.add(object);
    }

    public T change(@RequestBody T object) {
        validate(object);
        get(object.getId());
        return manager.change(object);
    }

    public void validate(T object) {

    }
}
