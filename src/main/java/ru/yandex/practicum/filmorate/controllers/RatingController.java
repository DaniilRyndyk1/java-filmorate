package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.services.RatingService;

@RequestMapping("/mpa")
@RestController
public class RatingController extends Controller<Rating> {
    public RatingController(RatingService service) {
        super(service);
    }
}
