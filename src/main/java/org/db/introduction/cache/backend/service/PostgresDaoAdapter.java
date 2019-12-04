package org.db.introduction.cache.backend.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.db.introduction.cache.backend.mapper.PersonRowMapper.SqlParametersBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class PostgresDaoAdapter {

    private final PostgresDao postgresDao;

    public List<Person> getAllPersons() {
        return ofNullable(postgresDao.selectAll())
            .orElse(Collections.emptyList());
    }

    public List<Person> getFirstNPersons(int amount) {
        var parameters = new PrintableMapSqlParameterSource()
            .addValue("amount", amount);
        return ofNullable(postgresDao.selectFirstN(parameters))
            .orElse(Collections.emptyList());
    }

    public Person getPersonById(long id) {
        var parameters = new PrintableMapSqlParameterSource()
            .addValue("id", id);

        return postgresDao.selectPersonById(parameters);
    }

    public void insertPerson(@NonNull Person person) {
        var parameters = new SqlParametersBuilder(person);
        postgresDao.insertPerson(parameters.getParameters());
    }
}
