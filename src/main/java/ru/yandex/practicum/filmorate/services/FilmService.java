package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService extends ModelService<Film> {

    private static final LocalDate MOVIE_BORNING_DATE = LocalDate.parse("1895-12-28");

    private final FilmDbStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserService userService) {
        super(filmStorage);
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void addLike(long filmId, long userId) {
        var film = get(filmId);
        var user = userService.get(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        var film = get(filmId);
        var user = userService.get(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        return filmStorage.getMostPopular(count);
    }

    @Override
    public void validate(Film object) {
        if (object.getName() == null) {
            throw new ValidationException("Название не задано", object);
        } else if (object.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым", object);
        } else if (object.getDescription() == null) {
            throw new ValidationException("Описание не задано", object);
        } else if (object.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно быть длиннее 200 символов", object);
        } else if (object.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не задана", object);
        } else if (object.getReleaseDate().isBefore(MOVIE_BORNING_DATE)) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895", object);
        } else if (object.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть больше 0", object);
        }
    }
}
