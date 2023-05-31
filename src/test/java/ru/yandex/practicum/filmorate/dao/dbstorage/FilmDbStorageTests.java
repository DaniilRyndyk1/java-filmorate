package ru.yandex.practicum.filmorate.dao.dbstorage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Rating;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final FilmDbStorage filmStorage;
    private final RatingDbStorage ratingStorage;
    private Rating rating;


    @BeforeEach
    public void setup() {
        rating = ratingStorage.add(new Rating(null, "test"));
    }

    @Test
    public void shouldFind() {
        var film = new Film("DanilaFilm2", "SuperDanila2", LocalDate.parse("2000-08-31"), 229);
        film.setMpa(rating);
        film = filmStorage.add(film);
        var film2 = filmStorage.find(film.getId());
        assertTrue(film2.isPresent());
        assertEquals(film2.get().getDuration(), 229);
    }

    @Test
    public void shouldRemove() {
        filmStorage.remove(1);
        var films = filmStorage.getAll();
        assertEquals(films.size(), 1);
    }

    @Test
    public void shouldCreate() {
        var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
        film.setMpa(rating);
        film = filmStorage.add(film);
        var films = filmStorage.getAll();

        assertEquals(films.size(), 2);
    }

    @Test
    public void shouldChange() {
        var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
        film.setMpa(rating);
        film = filmStorage.add(film);
        film.setName("king-kong");
        filmStorage.change(film);
        var film3 = filmStorage.find(film.getId());
        assertTrue(film3.isPresent());
        assertEquals(film3.get().getName(), "king-kong");
    }

    @Test
    public void shouldClear() {
        filmStorage.clear();
        var films = filmStorage.getAll();
        assertEquals(films.size(), 0);
    }
}