package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

@Component
public class InMemoryFilmStorage extends InMemoryModelStorage<Film> {
}
