package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
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
    private final FilmDbStorage filmDbStorage;

    @Test
    public void shouldFindFilm() {
        var film = new Film("DanilaFilm2", "SuperDanila2", LocalDate.parse("2000-08-31"), 229);
        film.setMpa(new Rating(1L, "AAAA"));
        filmDbStorage.add(film);
        var film2 = filmDbStorage.find(1);
        assertTrue(film2.isPresent());
        assertEquals(film2.get().getDuration(), 229);
    }

    @Test
    public void shouldRemoveFilm() {
        filmDbStorage.remove(1);
        var films = filmDbStorage.getAll();
        assertEquals(films.size(), 0);
    }

    @Test
    public void shouldCreateFilm() {
        var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
        film.setMpa(new Rating(1L, "AAAA"));
        filmDbStorage.add(film);
        var films = filmDbStorage.getAll();

        assertEquals(films.size(), 1);
    }

    @Test
    public void shouldChangeFilm() {
        var film = new Film("DanilaFilm", "SuperDanila", LocalDate.parse("2000-07-31"), 228);
        film.setMpa(new Rating(1L, "AAAA"));
        film = filmDbStorage.add(film);
        film.setName("king-kong");
        filmDbStorage.change(film);
        var film3 = filmDbStorage.find(3);
        assertTrue(film3.isPresent());
        assertEquals(film3.get().getName(), "king-kong");
    }

    @Test
    public void shouldClearFilms() {
        filmDbStorage.clear();
        var films = filmDbStorage.getAll();
        assertEquals(films.size(), 0);
    }
}