package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.*;


@Component
@Qualifier("FilmDao")
@Primary
public class FilmDao extends ModelDao<Film> {
    private final RatingDao ratingDao;
    private final GenreDao genreDao;

    public FilmDao(JdbcTemplate jdbcTemplate, RatingDao ratingDao, GenreDao genreDao) {
        super(jdbcTemplate, "FILM");
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
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

    public Set<Genre> getGenres(long id) {
        var result = new HashSet<Genre>();
        var rows = jdbcTemplate.queryForRowSet("select * from FILMS_GENRES WHERE film_id = " + id + " order by genre_id");
        while (rows.next()) {
            var genreId = rows.getLong("genre_id");
            var object = genreDao.find(genreId).get();
            result.add(object);
        }
        return result;
    }

    public void addGenre(Long id, Long genreId) {
        jdbcTemplate.execute("insert into films_genres values(" + id + ", " + genreId + ")");
    }

    public void removeGenre(Long id, Long genreId) {
        jdbcTemplate.execute("delete from films_genres where film_id = " + id + " and genre_id = " + genreId);
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
        var rating = ratingDao.find(ratingId);
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(rating.get());
        var genres = getGenres(id);
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
        object = super.add(object);
        for (Genre genre : object.getGenres()) {
            addGenre(object.getId(), genre.getId());
        }
        return object;
    }

    @Override
    public Film change(Film object) {
        jdbcTemplate.execute("delete from films_genres where film_id = " + object.getId());
        var gaenres = object.getGenres();
        var ids = new ArrayList<Long>();
        for (Genre genre : gaenres) {
            ids.add(genre.getId());
        }

        Collections.sort(ids);

        object.setGenres(new HashSet<>());
        object = super.change(object);
        for (Long id : ids) {
            addGenre(object.getId(), id);
            object.getGenres().add(genreDao.find(id).get());
        }
        return object;
    }
}
