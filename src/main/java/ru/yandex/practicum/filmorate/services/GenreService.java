package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dbstorage.GenreDbStorage;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.Set;

@Service
public class GenreService extends ModelService<Genre> {

    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage manager) {
        super(manager);
        this.genreDbStorage = manager;
    }

    public Set<Genre> getGenresByFiln(Long filmId) {
        return genreDbStorage.getGenresByFilm(filmId);
    }

    public void addGenreToFiln(Long id, Long filmId) {
        genreDbStorage.addGenreToFilm(id, filmId);
    }

    public void removeGenreToFiln(Long id, Long filmId) {
        genreDbStorage.removeGenreToFilm(id, filmId);
    }
}
