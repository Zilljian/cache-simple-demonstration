package org.db.introduction.cache.simple.demonstration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.simple.demonstration.mapper.PersonRowMapper;
import org.db.introduction.cache.simple.demonstration.model.Person;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.db.introduction.cache.simple.demonstration.util.LogInjector.debugLog;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PersonRowMapper personRowMapper;

    List<Person> selectAll() {
        return onRequest(
            () -> jdbcTemplate.query("SELECT id, name, surname, passport, town FROM hse.person", personRowMapper)
        );
    }

    List<Person> selectFirstN(MapSqlParameterSource parameters) {
        return onRequest(
            () -> jdbcTemplate.query(
                "SELECT id, name, surname, passport, town FROM hse.person LIMIT :amount", parameters,
                personRowMapper), parameters
        );
    }

    Person selectPersonById(MapSqlParameterSource parameters) {
        return onRequest(
            () -> jdbcTemplate.queryForObject(
                "SELECT id, name, surname, passport, town FROM hse.person WHERE id = :id", parameters,
                personRowMapper), parameters
        );
    }

    void insertPerson(MapSqlParameterSource parameters) {
        debugLog(
            () -> onRequest(
                () -> jdbcTemplate.update("INSERT INTO hse.person (id, name, surname, passport, town) "
                                              + "VALUES(:id, :name, :surname, :passport, :town)", parameters),
                parameters
            )
        );
    }

    private <R> R onRequest(Supplier<R> getDataCallback, Object... parameters) {
        try {
            log.info("Trying to query database with parameters: {}",
                     ofNullable(parameters)
                         .map(Objects::toString)
                         .orElse("no parameters"));
            return getDataCallback.get();
        } catch (DataAccessException e) {
            return null;
        }
    }
}
