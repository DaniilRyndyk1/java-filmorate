package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

@Service
public class RatingService extends ModelService<Rating> {

    @Autowired
    public RatingService(ModelStorage<Rating> manager) {
        super(manager);
    }
}
