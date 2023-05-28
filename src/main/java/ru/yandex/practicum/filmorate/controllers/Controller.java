package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storages.ModelStorage;
import ru.yandex.practicum.filmorate.models.Model;
import java.util.Map;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class Controller<T extends Model> {

    private final ModelStorage<T> manager;

    @GetMapping("{id}")
    public T get(@PathVariable long id) {
        var object = manager.find(id);
        if (object.isEmpty()) {
            throw new NotFoundException(id, object.getClass().getSimpleName());
        }
        return object.get();
    }

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
        get(object.getId());
        return manager.change(object);
    }

    @ExceptionHandler
    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Данные не корректны"
    )
    public Map<String, String> handleWrongData(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    public abstract void validate(T object);
}
