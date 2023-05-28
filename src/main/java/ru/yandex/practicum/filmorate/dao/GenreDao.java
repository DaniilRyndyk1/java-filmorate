package ru.yandex.practicum.filmorate.dao;
        import org.springframework.beans.factory.annotation.Qualifier;
        import org.springframework.context.annotation.Primary;
        import org.springframework.jdbc.core.JdbcTemplate;
        import org.springframework.jdbc.support.rowset.SqlRowSet;
        import org.springframework.stereotype.Component;
        import ru.yandex.practicum.filmorate.models.Genre;

        import java.util.ArrayList;
        import java.util.List;

@Component
@Qualifier("GenreDao")
@Primary
public class GenreDao extends ModelDao<Genre>{
    public GenreDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "GENRE");
    }

    @Override
    public Genre getObject(SqlRowSet set) {
        var id = set.getLong("ID");
        var name = set.getString("NAME");
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }

    @Override
    public String getInsertData(Genre object) {
        StringBuilder builder = new StringBuilder();
        builder.append("'").append(object.getId()).append("',").
                append("'").append(object.getName()).append("'");
        return builder.toString();
    }

    @Override
    public String getUpdateData(Genre object) {
        StringBuilder builder = new StringBuilder();
        builder.append("name = ").append("'").append(object.getName()).append("'");
        return builder.toString();
    }
}
