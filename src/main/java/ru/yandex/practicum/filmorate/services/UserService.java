package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userStorage;

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

    public User get(long userId) {
        var user = userStorage.find(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(userId, "User");
        }
        return user.get();
    }
}
