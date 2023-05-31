package ru.yandex.practicum.filmorate.dao.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.Rating;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTests {
    private final RatingDbStorage storage;

    @Test
    public void shouldRemove() {
        storage.remove(1);
        var users = storage.getAll();
        assertEquals(users.size(), 6);
    }

    @Test
    public void shouldFind() {
        var id = storage.add(new Rating(null, "Test3")).getId();
        var rating2 = storage.find(id);
        assertTrue(rating2.isPresent());
        assertEquals(rating2.get().getName(), "Test3");
    }

    @Test
    public void shouldCreate() {
        var rating = new Rating(null, "Test");
        storage.add(rating);
        var ratings = storage.getAll();

        assertEquals(ratings.size(), 7);
    }

    @Test
    public void shouldChange() {
        var rating = new Rating(0L, "Test");
        rating = storage.add(rating);
        rating.setName("Test2");
        storage.change(rating);
        var newRating = storage.find(6);
        assertTrue(newRating.isPresent());
        assertEquals(newRating.get().getName(), "Test2");
    }

    @Test
    public void shouldClear() {
        storage.clear();
        var ratings = storage.getAll();
        assertEquals(ratings.size(), 0);
    }
}
