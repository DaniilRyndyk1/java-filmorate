package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Genre extends Model {
    public Genre(Long id, String name) {
        this.setId(id);
        this.setName(name);
    }
}
