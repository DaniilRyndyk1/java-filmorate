package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;
import ru.yandex.practicum.filmorate.services.RatingService;
import ru.yandex.practicum.filmorate.storages.ModelStorage;

import java.util.*;


@Component
@Primary
@Slf4j
public class FilmDbStorage implements ModelStorage<Film> {
    private final RatingService ratingService;
    private final GenreService genreService;
    public final JdbcTemplate jdbcTemplate;
    public final String tableName = "FILM";
    public long id = 1;

    @Autowired
    public FilmDbStorage(JdbcTemplate template, RatingService ratingService, GenreService genreService) {
        this.ratingService = ratingService;
        this.genreService = genreService;
        this.jdbcTemplate = template;
    }

    @Override
    public Optional<Film> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from " + tableName + " where id =" + id);
        if (userRows.next()) {
            var result = getObject(userRows);
            return Optional.of(result);
        } else {
            log.info("Объект с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean remove(long id) {
        jdbcTemplate.execute("delete from " + tableName + " where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from " + tableName);
    }

    @Override
    public List<Film> getAll() {
        var result = new ArrayList<Film>();
        var rows = jdbcTemplate.queryForRowSet("select * from " + tableName);
        while (rows.next()) {
            var object = getObject(rows);
            result.add(object);
        }
        return result;
    }

    public List<Film> getMostPopular(Integer count) {
        var result = new ArrayList<Film>();
        var rows =  jdbcTemplate.queryForRowSet("SELECT f.ID \n" +
                "FROM LIKES l \n" +
                "RIGHT JOIN FILM f ON l.FILM_ID = f.ID \n" +
                "GROUP BY f.ID \n" +
                "ORDER BY COUNT(l.USER_ID) DESC \n" +
                "LIMIT " + count);
        while (rows.next()) {
            var id = rows.getLong("id");
            var object = find(id).get();
            result.add(object);
        }
        return result;
    }

    public List<Long> getLikes(long id) {
        var result = new ArrayList<Long>();
        var rows = jdbcTemplate.queryForRowSet("select * from Likes WHERE film_id = " + id);
        while (rows.next()) {
            var object = rows.getLong("user_id");
            result.add(object);
        }
        return result;
    }

    public void addLike(Long id, Long userId) {
        jdbcTemplate.execute("insert into Likes values(" + userId + ", " + id + ")");
    }

    public void removeLike(Long id, Long userId) {
        jdbcTemplate.execute("delete from Likes where film_id = " + id + " and user_id = " + userId);
    }

    @Override
    public Film getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var description = set.getString("DESCRIPTION");
        var releaseDate = Objects.requireNonNull(set.getDate("REALEASE_DATE")).toLocalDate();
        var duration = set.getInt("DURATION");
        var ratingId = set.getLong("Rating_id");
        var name = set.getString("NAME");
        var rating = ratingService.get(ratingId);
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(rating);
        var genres = genreService.getGenresByFiln(id);
        film.setGenres(genres);
        return film;
    }

    @Override
    public String getInsertData(Film object) {
        return "'" +
                object.getId() +
                "'," +
                "'" +
                object.getDescription() +
                "'," +
                "'" +
                object.getReleaseDate() +
                "'," +
                "'" +
                object.getDuration() +
                "'," +
                "'" +
                object.getMpa().getId() +
                "'," +
                "'" +
                object.getName() +
                "'";
    }

    @Override
    public String getUpdateData(Film object) {
        return "description = " +
                "'" +
                object.getDescription() +
                "'," +
                "realease_Date = " +
                "'" +
                object.getReleaseDate() +
                "'," +
                "duration = " +
                "'" +
                object.getDuration() +
                "'," +
                "rating_Id = " +
                "'" +
                object.getMpa().getId() +
                "'," +
                "name = " +
                "'" +
                object.getName() +
                "'";
    }

    @Override
    public Film add(Film object) {
        object.setId(id);
        jdbcTemplate.execute("insert into PUBLIC." + tableName + " values(" + getInsertData(object) + ")");
        log.info("Успешно был добавлен объект типа {} с id = {}", object.getClass().getSimpleName(), id);
        id++;
        for (Genre genre : object.getGenres()) {
            genreService.addGenreToFiln(object.getId(), genre.getId());
        }
        return object;
    }

    @Override
    public Film change(Film object) {
        jdbcTemplate.execute("delete from films_genres where film_id = " + object.getId());
        var genres = object.getGenres();
        var ids = new ArrayList<Long>();
        for (Genre genre : genres) {
            ids.add(genre.getId());
        }

        Collections.sort(ids);

        object.setGenres(new HashSet<>());
        jdbcTemplate.execute("update " + tableName + " set " + getUpdateData(object) + " where id = " + object.getId());
        for (Long id : ids) {
            genreService.addGenreToFiln(object.getId(), id);
            object.getGenres().add(genreService.get(id));
        }
        return object;
    }
}
