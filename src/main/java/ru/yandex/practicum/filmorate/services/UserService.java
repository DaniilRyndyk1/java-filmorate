package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    public void addFriendship(User user1, User user2) {
        user1.addFriend(user2.getId());
        user2.addFriend(user1.getId());
    }

    public void removeFriendship(User user1, User user2) {
        user1.removeFriend(user2.getId());
        user2.removeFriend(user1.getId());
    }

    public List<Long> getSameFriends(User user1, User user2) {
        var user1Friends = user1.getFriends();
        var user2Friends = user2.getFriends();
        var sameFriends = new ArrayList<Long>();
        for (long id : user1Friends) {
            if (user2Friends.contains(id)) {
                sameFriends.add(id);
            }
        }
        return sameFriends;
    }
}
