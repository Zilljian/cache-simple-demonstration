package org.db.introduction.cache.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
public class Student {

    private Long id;
    private boolean phone;
    private String university;
    private Long personId;

    @Component
    public static class RowMapper implements org.springframework.jdbc.core.RowMapper<Student> {

        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            return builder()
                    .id(rs.getLong("id"))
                    .phone(rs.getBoolean("phone"))
                    .personId(rs.getLong("person_id"))
                    .university(rs.getString("university"))
                    .build();
        }
    }

    @JsonIgnore
    public MapSqlParameterSource getParameters() {
        return new PrintableMapSqlParameterSource()
                .addValue("id", getId())
                .addValue("university", getUniversity())
                .addValue("personId", getPersonId())
                .addValue("phone", isPhone());
    }
}
