package ru.yandex.practicum.filmorate.models;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends Model{
    private String description;
    private LocalDate releaseDate;
    private int duration;
    public Film(String name, String description, LocalDate releaseDate, int duration){
        this.setName(name);
        this.setDescription(description);
        this.setReleaseDate(releaseDate);
        this.setDuration(duration);
    }
}
