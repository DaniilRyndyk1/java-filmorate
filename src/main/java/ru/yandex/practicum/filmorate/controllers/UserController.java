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
@RequestMapping("/api/users")
public class UserController extends Controller<User> {
    private static final LocalDate CURRENT_DATE = LocalDate.now();

    public UserController() {
        manager = Managers.userManager;
    }

    @Override
    public void validate(User object) throws ValidationException {
        if (object.getEmail().equals("")) {
            throw new ValidationException("Электронная почта не может быть пустой", object);
        } else if (!object.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @", object);
        } else if (object.getLogin().equals("")) {
            throw new ValidationException("Логин не может быть пустым", object);
        } else if (object.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы", object);
        } else if (object.getBirthday().isAfter(CURRENT_DATE)) {
            throw new ValidationException("Дата рождения не может быть в будущем", object);
        }
    }
}

