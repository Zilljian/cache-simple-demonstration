package org.db.introduction.cache.simple.demonstration.mapper;

import org.db.introduction.cache.simple.demonstration.model.Person;
import org.springframework.jdbc.core.RowMapper;
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
}
