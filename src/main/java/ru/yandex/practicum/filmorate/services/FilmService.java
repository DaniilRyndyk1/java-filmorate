package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmStorage;
    private final UserService userService;

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

    public Film get(long id) {
        var film = filmStorage.find(id);
        if (film.isEmpty()) {
            throw new NotFoundException(id, "Film");
        }
        return film.get();
    }
}
