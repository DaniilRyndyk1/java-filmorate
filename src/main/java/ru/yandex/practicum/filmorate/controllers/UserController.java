package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {

    private final UserService service;

    @Autowired
    public UserController(UserService service, UserDao storage) {
        super(storage);
        this.service = service;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable int friendId) {
       service.addFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable int friendId) {
        service.removeFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSameFriends(@PathVariable long id, @PathVariable long otherId) {
       return service.getSameFriends(id, otherId);
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

