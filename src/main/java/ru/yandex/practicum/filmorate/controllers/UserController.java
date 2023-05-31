package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        super(service);
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
}

