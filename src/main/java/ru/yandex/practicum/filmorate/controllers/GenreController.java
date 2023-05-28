package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.models.Genre;


@RequestMapping("/genres")
@RestController
public class GenreController extends Controller<Genre> {
    public GenreController(GenreDao manager) {
        super(manager);
    }

    @Override
    public void validate(Genre object) {

    }
}
