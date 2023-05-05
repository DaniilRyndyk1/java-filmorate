package ru.yandex.practicum.filmorate.models;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Model {
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Long> friends;

    public User(String name, String email, String login, LocalDate date) {
        this.setLogin(login);
        this.setName(name);
        this.setEmail(email);
        this.setBirthday(date);
        friends = new HashSet<>();
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            super.setName(this.login);
        } else {
            super.setName(name);
        }
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
