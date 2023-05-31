package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Model;
import ru.yandex.practicum.filmorate.dao.ModelStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class ModelService<T extends Model> {
    private final ModelStorage<T> storage;

    public T get(@PathVariable long id) {
        var object = storage.find(id);
        if (object.isEmpty()) {
            throw new NotFoundException(id, object.getClass().getSimpleName());
        }
        return object.get();
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public void clear() {
        storage.clear();
    }

    public T create(@RequestBody T object) {
        validate(object);
        return storage.add(object);
    }

    public T change(@RequestBody T object) {
        validate(object);
        get(object.getId());
        return storage.change(object);
    }

    public void validate(T object) {

    }
}
