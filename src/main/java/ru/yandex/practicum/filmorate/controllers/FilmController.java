package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    private static final LocalDate MOVIE_BORNING_DATE = LocalDate.parse("1895-12-28");
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service, FilmDao storage, UserService userService) {
        super(storage);
        this.service = service;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam("count") Optional<Integer> count) {
        int filmsCount = 10;
        if (count.isPresent()) {
            filmsCount = count.get();
        }
        return service.getMostPopular(filmsCount);
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
