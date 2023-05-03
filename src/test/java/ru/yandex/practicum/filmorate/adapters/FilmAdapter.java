package ru.yandex.practicum.filmorate.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.filmorate.models.Film;

import java.io.IOException;
import java.time.LocalDate;

public class FilmAdapter extends Adapter<Film> {

    public FilmAdapter() {
        super(new Film("","", LocalDate.now(),1));
    }

    @Override
    public void write(JsonWriter writer, Film film) {
        try {
            writer.beginObject();
            super.write(writer, film);
            writeProperty(writer, "duration", film.getDuration() + "");
            writeProperty(writer, "releaseDate", film.getReleaseDate() + "");
            writeProperty(writer, "description", film.getDescription());
            writer.endObject();
        } catch (IOException exception) {
            System.out.println("Во время сериализации задачи возникло исключение: " + exception.getMessage());
        }
    }

    @Override
    protected boolean setProperty(String name, Film film, JsonReader reader) throws IOException {
        var isStandard = super.setProperty(name, film, reader);
        if (!isStandard) {
            switch (name) {
                case "duration":
                    film.setDuration(reader.nextInt());
                    break;
                case "description":
                    film.setDescription(reader.nextString());
                    break;
                case "releaseDate":
                    var releaseDate = LocalDate.parse(reader.nextString(), formatter);
                    film.setReleaseDate(releaseDate);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}