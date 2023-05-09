package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelStorage<User> userStorage;

    public void addFriendship(long userId, long friendId) {
        var user = get(userId);
        var friend = get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriendship(long userId, long friendId) {
        var user = get(userId);
        var friend = get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(long id) {
        var user = get(id);
        var friends = new ArrayList<User>();
        var friendsIds = user.getFriends();
        for (long friendId : friendsIds) {
            var friend = get(friendId);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> getSameFriends(long id, long otherId) {
        var user = get(id);
        var otherUser = get(otherId);
        var otherFriends = otherUser.getFriends();
        return user
                .getFriends()
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
