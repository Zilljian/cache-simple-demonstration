package org.db.introduction.cache.backend.mapper;

import lombok.RequiredArgsConstructor;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Person.builder()
            .id((long) rs.getInt("id"))
            .name(rs.getString("name"))
            .surname(rs.getString("surname"))
            .passport(rs.getString("passport"))
            .town(rs.getString("town"))
            .build();
    }

    @RequiredArgsConstructor
    public static class SqlParametersBuilder {

        private final Person person;

        public MapSqlParameterSource getParameters() {
            return new PrintableMapSqlParameterSource()
                .addValue("name", person.getName())
                .addValue("surname", person.getSurname())
                .addValue("passport", person.getPassport())
                .addValue("town", person.getTown());
        }
    }
}
