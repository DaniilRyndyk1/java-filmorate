package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.managers.Managers;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    private static final LocalDate MOVIE_BORNING_DATE = LocalDate.parse("1895-12-28");

    public FilmController() {
        manager = Managers.filmManager;
    }

    @Override
    public void validate(Film object) throws ValidationException {
        if (object.getName().equals("")) {
            throw new ValidationException("Название не может быть пустым", object);
        } else if (object.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно быть длиннее 200 символов", object);
        } else if (object.getReleaseDate().isBefore(MOVIE_BORNING_DATE)) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895", object);
        } else if (object.getDuration() <= 0) {
            throw new ValidationException("Продолжительность должна быть больше 0", object);
        }
    }
}
