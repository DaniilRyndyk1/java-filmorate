package ru.yandex.practicum.filmorate.exceptions;

import ru.yandex.practicum.filmorate.models.Model;

public class ValidationException extends Exception {

    private final Model model;

    public Model getModel() {
        return model;
    }

    public ValidationException(String message, Model model) {
        super("При создании объекта типа "+model.getClass().getSimpleName() + ":"+ message);
        this.model = model;
    }
}
