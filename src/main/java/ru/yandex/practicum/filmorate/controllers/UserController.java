package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.managers.Managers;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {

    public UserController() {
        manager = Managers.userManager;
    }

    @Override
    public void validate(User object) {
        if (object.getEmail() == null) {
            throw new ValidationException("Email не задан", object);
        } else if (object.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой", object);
        }  else if (!object.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @", object);
        } else if (object.getLogin() == null) {
            throw new ValidationException("Логин не может отсутствовать", object);
        } else if (object.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым", object);
        } else if (object.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы", object);
        } else if (object.getBirthday() == null) {
            throw new ValidationException("День рождения не может отсутствовать", object);
        } else if (object.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем", object);
        }
    }
}

