package ru.yandex.practicum.filmorate.dao.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

@Component
public class InMemoryFilmStorage extends InMemoryModelStorage<Film> {
}
