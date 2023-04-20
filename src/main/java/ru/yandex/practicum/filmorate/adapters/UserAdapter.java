package ru.yandex.practicum.filmorate.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.filmorate.models.User;

import java.io.IOException;
import java.time.LocalDate;

public class UserAdapter extends Adapter<User> {

    public UserAdapter() {
        super(new User("", "","", LocalDate.now()));
    }

    @Override
    public void write(JsonWriter writer, User user) {
        try {
            writer.beginObject();
            super.write(writer, user);
            writeProperty(writer, "email", user.getEmail());
            writeProperty(writer, "login", user.getLogin() );
            writeProperty(writer, "birthday", user.getBirthday() + "");
            writer.endObject();
        } catch (IOException exception) {
            System.out.println("Во время сериализации пользователя возникло исключение: " + exception.getMessage());
        }
    }

    @Override
    public boolean setProperty(String name, User user, JsonReader reader) throws IOException {
        switch (name) {
            case "name":
                user.setName(reader.nextString());
                break;
            case "email":
                user.setEmail(reader.nextString());
                break;
            case "login":
                user.setLogin(reader.nextString());
                break;
            case "birthday":
                var date = LocalDate.parse(reader.nextString(),super.formatter);
                user.setBirthday(date);
                break;
            default:
                return false;
        }
        return true;
    }
}
