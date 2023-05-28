package ru.yandex.practicum.filmorate.dao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Rating;

@Component
@Qualifier("RatingDao")
@Primary
public class RatingDao extends ModelDao<Rating>{
    public RatingDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "Rating");
    }

    @Override
    public Rating getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var name = set.getString("NAME");
        Rating rating = new Rating();
        rating.setId(id);
        rating.setName(name);
        return rating;
    }

    @Override
    public String getInsertData(Rating object) {
        return "'" +
                object.getId() +
                "'," +
                "'" +
                object.getName() +
                "'";
    }

    @Override
    public String getUpdateData(Rating object) {
        return "name = " +
                "'" +
                object.getName() +
                "'";
    }
}