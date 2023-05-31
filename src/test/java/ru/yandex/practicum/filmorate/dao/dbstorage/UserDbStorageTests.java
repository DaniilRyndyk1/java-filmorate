package ru.yandex.practicum.filmorate.dao.dbstorage;

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
    private final UserDbStorage storage;

    @Test
    public void shouldRemove() {
        storage.remove(1);
        var users = storage.getAll();
        assertEquals(users.size(), 1);
    }

    @Test
    public void shouldFind() {
        var user = storage.add(new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31")));
        var user2 = storage.find(user.getId());
        assertTrue(user2.isPresent());
        assertEquals(user2.get().getLogin(), "Danila228");
    }

    @Test
    public void shouldCreate() {
        var user = new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31"));
        user = storage.add(user);
        var users = storage.getAll();

        assertEquals(users.size(), 2);
    }

    @Test
    public void shouldChange() {
        var user = storage.add(new User("Danila", "danila@gmail.com", "Danila228", LocalDate.parse("2000-07-31")));
        var id = user.getId();
        user.setName("Nikita");
        storage.change(user);
        var user2 = storage.find(id);
        assertTrue(user2.isPresent());
        assertEquals(user2.get().getName(), "Nikita");
    }

    @Test
    public void shouldClear() {
        storage.clear();
        var users = storage.getAll();
        assertEquals(users.size(), 0);
    }
}
