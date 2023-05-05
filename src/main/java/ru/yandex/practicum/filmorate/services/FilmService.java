package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final ModelStorage<Film> filmStorage;

    public void addLike(Film film, User user) {
        film.addLike(user.getId());
    }

    public void removeLike(Film film, User user) {
        film.removeLike(user.getId());
    }

    public Set<Film> getMostPopular(Integer count) {
        var films = filmStorage.getAll();
        var filmsLikesCount = new HashMap<Film, Long>();
        for (Film film : films) {
            long countLikes = film.getLikes().size();
            filmsLikesCount.put(film, countLikes);
        }
        return filmsLikesCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(count).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();

    }
}
