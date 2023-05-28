package ru.yandex.practicum.filmorate.models;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends Model {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes;
    private Rating mpa;
    private Set<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.setName(name);
        this.setDescription(description);
        this.setReleaseDate(releaseDate);
        this.setDuration(duration);
        likes = new HashSet<>();
        //mpa = new HashSet<>();
        genres = new HashSet<>();
    }

    public void addLike(Long id) {
        likes.add(id);
    }

    public void removeLike(Long id) {
        likes.remove(id);
    }
}
