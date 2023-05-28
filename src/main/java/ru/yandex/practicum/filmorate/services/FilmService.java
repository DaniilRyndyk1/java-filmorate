package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final ModelStorage<Film> filmStorage;
    private final UserService userService;

    public void addLike(long filmId, long userId) {
        var film = get(filmId);
        var user = userService.get(userId);
        film.addLike(userId);
    }

    public void removeLike(long filmId, long userId) {
        var film = get(filmId);
        var user = userService.get(userId);
        film.removeLike(userId);
    }

    public Set<Film> getMostPopular(Integer count) {
        return filmStorage
                .getAll()
                .stream()
                .collect(Collectors.toMap(x -> x, x -> x.getLikes().size()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet();
    }

    public Film get(long id) {
        var film = filmStorage.find(id);
        if (film.isEmpty()) {
            throw new NotFoundException(id, "Film");
        }
        return film.get();
    }
}
