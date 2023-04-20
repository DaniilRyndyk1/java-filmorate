package ru.yandex.practicum.filmorate.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.yandex.practicum.filmorate.models.Model;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.time.format.DateTimeFormatter;

public class Adapter<T extends Model> extends TypeAdapter<T> {
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final T model;

    public Adapter(T model) {
        this.model = model;
    }

    @Override
    public void write(JsonWriter writer, T t) {
        try {
            writeProperty(writer, "id", t.getId() + "");
            writeProperty(writer, "name", t.getName());
        } catch (IOException exception) {
            System.out.println("Во время сериализации объекта возникло исключение: " + exception.getMessage());
        }

    }

    @Override
    public T read(JsonReader reader) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                setModelProperty(model, reader);
            }
            reader.endObject();

            var hasName = !model.getName().equals("");

            if (hasName) {
                return model;
            } else {
                throw new InterruptedIOException("На вход поступили некорректные данные");
            }
        } catch (IOException exception) {
            System.out.println("Во время десериализации возникло исключение: " + exception.getMessage());
            return null;
        }
    }

    protected void setModelProperty(T model, JsonReader reader) throws IOException {
        var token = reader.nextName().toLowerCase();
        if (!token.equals("")) {
            reader.peek();
            var isCompleted = setProperty(token, model, reader);
            if (!isCompleted) {
                reader.skipValue();
            }
        }
    }

    protected boolean setProperty(String name, T model, JsonReader reader) throws IOException {
        if (name.equals("name")) {
            model.setName(reader.nextString());
            return true;
        }
        return false;

    }

    protected void writeProperty(JsonWriter writer, String name, String value) throws IOException {
        writer.name(name);
        writer.value(value);
    }
}
