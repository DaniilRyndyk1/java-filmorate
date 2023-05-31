package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dbstorage.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends ModelService<User> {

    private final UserDbStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        super(userStorage);
        this.userStorage = userStorage;
    }

    public void addFriendship(long userId, long friendId) {
        get(userId);
        get(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriendship(long userId, long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long id) {
        var friends = new ArrayList<User>();
        var friendsIds = userStorage.getFriends(id);
        for (long friendId : friendsIds) {
            var friend = get(friendId);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> getSameFriends(long id, long otherId) {
        var otherFriends = userStorage.getFriends(otherId);
        return userStorage
                .getFriends(id)
                .stream()
                .filter(otherFriends::contains)
                .map(this::get)
                .collect(Collectors.toList());
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
