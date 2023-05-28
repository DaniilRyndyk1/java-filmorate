package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.models.Rating;

@RequestMapping("/mpa")
@RestController
public class RatingController extends Controller<Rating> {
    public RatingController(RatingDao manager) {
        super(manager);
    }

    @Override
    public void validate(Rating object) {

    }
}
