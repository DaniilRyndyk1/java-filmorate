package ru.yandex.practicum.filmorate.models;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Model {
    private String email;
    private String login;
    private LocalDate birthday;
    public User(String name, String email, String login, LocalDate date){
        this.setName(name);
        this.setEmail(email);
        this.setLogin(login);
        this.setBirthday(date);
    }
}
