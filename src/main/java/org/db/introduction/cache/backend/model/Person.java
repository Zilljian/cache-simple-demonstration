package org.db.introduction.cache.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@Builder
public class Person {

    private Long id;
    private String name;
    private String surname;
    private String passport;
    private String phone;
    private Long externalId;
    private LocalDateTime created;
    private LocalDateTime updated;

    @Component
    public static class RowMapper implements org.springframework.jdbc.core.RowMapper<Person> {

        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            return builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .surname(rs.getString("surname"))
                    .passport(rs.getString("passport"))
                    .phone(rs.getString("phone"))
                    .externalId(rs.getLong("external_id"))
                    .created(rs.getTimestamp("created").toLocalDateTime())
                    .updated(rs.getTimestamp("updated").toLocalDateTime())
                    .build();
        }
    }

    @JsonIgnore
    public MapSqlParameterSource getParameters() {
        return new PrintableMapSqlParameterSource()
                .addValue("id", getId())
                .addValue("name", getName())
                .addValue("surname", getSurname())
                .addValue("passport", getPassport())
                .addValue("phone", getPhone())
                .addValue("externalId", getExternalId());
    }
}
