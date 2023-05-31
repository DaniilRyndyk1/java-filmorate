package ru.yandex.practicum.filmorate.dao.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {
    private final GenreDbStorage storage;

    @Test
    public void shouldRemove() {
        storage.remove(1);
        assertEquals(storage.getAll().size(), 7);
    }

    @Test
    public void shouldFind() {
        var id = storage.add(new Genre(null, "Test3")).getId();
        var genre = storage.find(id);
        assertTrue(genre.isPresent());
        assertEquals(genre.get().getName(), "Test3");
    }

    @Test
    public void shouldCreate() {
        var genre = new Genre(null, "Test");
        storage.add(genre);

        assertEquals(storage.getAll().size(), 8);
    }

    @Test
    public void shouldChange() {
        var genre = new Genre(0L, "Test");
        genre = storage.add(genre);
        genre.setName("Test2");
        storage.change(genre);
        var newGenre = storage.find(genre.getId());
        assertTrue(newGenre.isPresent());
        assertEquals(newGenre.get().getName(), "Test2");
    }

    @Test
    public void shouldClear() {
        storage.clear();
        assertEquals(storage.getAll().size(), 0);
    }
}
