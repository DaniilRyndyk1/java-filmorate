package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.managers.ModelManager;
import ru.yandex.practicum.filmorate.models.Model;

import java.util.List;
@Slf4j
public class Controller <T extends Model> {
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
        try {
            validate(object);
        } catch (ValidationException e) {
            log.error("При валидации возникло исключение:", e);
            return null;
        }
        var foundObject = manager.find(object.getId());
        if(foundObject.isEmpty()){
            return manager.add(object);
        }
        return manager.change(object);
    }
    public void validate(T object) throws ValidationException {
        throw new UnsupportedOperationException();
    }
}
