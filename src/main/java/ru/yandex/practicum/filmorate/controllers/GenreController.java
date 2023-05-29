package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;


@RequestMapping("/genres")
@RestController
public class GenreController extends Controller<Genre> {
    public GenreController(GenreService service) {
        super(service);
    }
}
