package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.managers.ModelManager;
import ru.yandex.practicum.filmorate.models.Model;

import java.util.List;

@Slf4j
public abstract class Controller<T extends Model> {

    protected ModelManager<T> manager;

    @GetMapping
    public List<T> getAll() {
        return manager.getAll();
    }

    @DeleteMapping
    public void clear() {
         manager.clear();
    }

    @PostMapping
    public T create(@RequestBody T object) {
        validate(object);
        return manager.add(object);
    }

    @PutMapping
    public T change(@RequestBody T object) {
        validate(object);
        var foundObject = manager.find(object.getId());
        if (foundObject.isEmpty()) {
            var exception = new ValidationException("Такой объект не существует", object);
            log.error("Такой объект не существует", exception);
            throw exception;
        }
        return manager.change(object);
    }

    public abstract void validate(T object);
}
