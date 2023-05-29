package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.services.ModelService;
import ru.yandex.practicum.filmorate.models.Model;
import java.util.Map;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class Controller<T extends Model> {

    private final ModelService<T> service;

    @GetMapping("{id}")
    public T get(@PathVariable long id) {
        return service.get(id);
    }

    @GetMapping
    public List<T> getAll() {
        return service.getAll();
    }

    @DeleteMapping
    public void clear() {
         service.clear();
    }

    @PostMapping
    public T create(@RequestBody T object) {
        return service.create(object);
    }

    @PutMapping
    public T change(@RequestBody T object) {
        return service.change(object);
    }

    @ExceptionHandler
    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Данные не корректны"
    )
    public Map<String, String> handleWrongData(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    public void validate(T object) {
        service.validate(object);
    }
}
