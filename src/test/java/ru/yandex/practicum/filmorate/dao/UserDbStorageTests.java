package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;

    @Test
    public void shouldRemoveUser() {
        userDbStorage.remove(1);
        var users = userDbStorage.getAll();
        assertEquals(users.size(), 0);
    }

    @Test
    public void shouldFindUser() {
        var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
        userDbStorage.add(user);
        var user2 = userDbStorage.find(1);
        assertTrue(user2.isPresent());
        assertEquals(user2.get().getLogin(), "Danila228");
    }

    @Test
    public void shouldCreateUser() {
        var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
        user = userDbStorage.add(user);
        var users = userDbStorage.getAll();

        assertEquals(users.size(), 1);
    }

    @Test
    public void shouldChangeUser() {
        var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
        user = userDbStorage.add(user);
        user.setName("Nikita");
        userDbStorage.change(user);
        var user3 = userDbStorage.find(3);
        assertTrue(user3.isPresent());
        assertEquals(user3.get().getName(), "Nikita");
    }

    @Test
    public void shouldClearUsers() {
        userDbStorage.clear();
        var users = userDbStorage.getAll();
        assertEquals(users.size(), 0);
    }
}
