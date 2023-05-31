package ru.yandex.practicum.filmorate.dao.dbstorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ModelStorage;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.*;

@Component
@Primary
@Slf4j
public class GenreDbStorage implements ModelStorage<Genre> {

    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre add(Genre object) {
        jdbcTemplate.execute("insert into PUBLIC.GENRE (name) values(" + getInsertData(object) + ")");
        var rows = jdbcTemplate.queryForRowSet("select id from Genre order by id desc limit 1");
        rows.next();
        var id = rows.getLong("id");
        object.setId(id);
        log.info("Успешно был добавлен объект типа {}", object.getClass().getSimpleName());
        return object;
    }

    @Override
    public Genre change(Genre object) {
        jdbcTemplate.execute("update GENRE set " + getUpdateData(object) + " where id = " + object.getId());
        return object;
    }

    @Override
    public Optional<Genre> find(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from GENRE where id =" + id);
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
        jdbcTemplate.execute("delete from GENRE where id = " + id);
        return true;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from GENRE");
    }

    @Override
    public List<Genre> getAll() {
        var result = new ArrayList<Genre>();
        var rows = jdbcTemplate.queryForRowSet("select * from GENRE");
        while (rows.next()) {
            var object = getObject(rows);
            result.add(object);
        }
        return result;
    }

    public Set<Genre> getGenresByFilm(long filmId) {
        var result = new HashSet<Genre>();
        var rows = jdbcTemplate.queryForRowSet("select * from FILMS_GENRES WHERE film_id = " + filmId + " order by genre_id");
        while (rows.next()) {
            var genreId = rows.getLong("genre_id");
            var object = find(genreId).get();
            result.add(object);
        }
        return result;
    }

    public void addGenreToFilm(Long filmId, Long genreId) {
        jdbcTemplate.execute("insert into films_genres values(" + filmId + ", " + genreId + ")");
    }

    public void removeGenreToFilm(Long filmId, Long genreId) {
        jdbcTemplate.execute("delete from films_genres where film_id = " + filmId + " and genre_id = " + genreId);
    }

    @Override
    public Genre getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var name = set.getString("NAME");
        return new Genre(id, name);
    }

    @Override
    public String getInsertData(Genre object) {
        return  "'" +
                object.getName() +
                "'";
    }

    @Override
    public String getUpdateData(Genre object) {
        return "name = " +
                "'" +
                object.getName() +
                "'";
    }
}
