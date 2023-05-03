package ru.yandex.practicum.filmorate.managers;
import ru.yandex.practicum.filmorate.models.*;

public class Managers {
    public  static ModelManager<User> userManager = new InMemoryModelManager();
    public  static ModelManager<Film> filmManager = new InMemoryModelManager();
}
