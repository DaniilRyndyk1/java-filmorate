package ru.yandex.practicum.filmorate.dao.inmemorystorage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

@Component
public class InMemoryUserStorage extends InMemoryModelStorage<User> {
}
