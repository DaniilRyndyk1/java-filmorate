package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dbstorage.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService extends ModelService<Film> {

    private static final LocalDate MOVIE_BORNING_DATE = LocalDate.parse("1895-12-28");
    private final FilmDbStorage filmStorage;
    private final UserService userService;
    private final RatingService ratingService;
    private final GenreService genreService;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserService userService, RatingService ratingService, GenreService genreService) {
        super(filmStorage);
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.ratingService = ratingService;
        this.genreService = genreService;
    }

    public void addLike(long filmId, long userId) {
        get(filmId);
        userService.get(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        get(filmId);
        userService.get(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopular(Integer count) {
        return getFilmsWithData(filmStorage.getMostPopular(count));
    }

    @Override
    public Film get(long id) {
        return getFilmWithData(id);
    }

    @Override
    public List<Film> getAll() {
        return getFilmsWithData(super.getAll());
    }

    @Override
    public Film create(Film object) {
        object = super.create(object);
        for (Genre genre : object.getGenres()) {
            genreService.addGenreToFiln(object.getId(), genre.getId());
        }
        return object;
    }

    @Override
    public Film change(Film object) {
        var finalObject = super.change(object);
        var genres = object.getGenres();
        finalObject.setGenres(new HashSet<>());
        genres.stream().mapToLong(Genre::getId).sorted().forEach((x) -> {
            genreService.addGenreToFiln(finalObject.getId(), x);
            finalObject.getGenres().add(genreService.get(x));
        });
        return finalObject;
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

    private Film getFilmWithData(long id) {
        var film = super.get(id);
        var ratingId = film.getMpa().getId();
        var rating = ratingService.get(ratingId);
        film.setMpa(rating);

        var genres = genreService.getGenresByFiln(film.getId());
        film.setGenres(genres);

        return film;
    }

    private List<Film> getFilmsWithData(List<Film> films) {
        for (var i = 0; i < films.size(); i++) {
            var id = films.get(i).getId();
            films.remove(i);
            films.add(i, getFilmWithData(id));
        }
        return films;
    }
}
