package ru.yandex.practicum.filmorate.models;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Rating extends Model {
    public Rating(Long id, String name) {
        this.setId(id);
        this.setName(name);
    }
}
