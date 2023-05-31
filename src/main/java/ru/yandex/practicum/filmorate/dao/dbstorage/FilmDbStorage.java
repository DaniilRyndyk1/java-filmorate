package ru.yandex.practicum.filmorate.dao.dbstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ModelStorage;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Rating;

import java.util.*;


@Component
@Primary
@Slf4j
public class FilmDbStorage implements ModelStorage<Film> {
    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    @Override
    public Optional<Film> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from public.FILM where id =" + id);
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
        jdbcTemplate.execute("delete from public.FILM where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from public.FILM");
    }

    @Override
    public List<Film> getAll() {
        var result = new ArrayList<Film>();
        var rows = jdbcTemplate.queryForRowSet("select * from public.FILM");
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
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(new Rating(ratingId, ""));
        return film;
    }

    @Override
    public String getInsertData(Film object) {
        return
                "'" +
                object.getName() +
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
        jdbcTemplate.execute("insert into PUBLIC.FILM (name, DESCRIPTION, REALEASE_DATE, DURATION, RATING_ID) values(" + getInsertData(object) + ")");
        var rows = jdbcTemplate.queryForRowSet("select id from public.Film order by id desc limit 1");
        rows.next();
        var id = rows.getLong("id");
        object.setId(id);
        log.info("Успешно был добавлен объект типа {}", object.getClass().getSimpleName());
        return object;
    }

    @Override
    public Film change(Film object) {
        jdbcTemplate.execute("delete from films_genres where film_id = " + object.getId());
        jdbcTemplate.execute("update public.FILM set " + getUpdateData(object) + " where id = " + object.getId());
        return object;
    }
}
