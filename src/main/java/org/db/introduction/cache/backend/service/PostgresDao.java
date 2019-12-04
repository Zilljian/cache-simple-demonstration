package org.db.introduction.cache.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.backend.mapper.PersonRowMapper;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.util.LogInjector;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PersonRowMapper personRowMapper;

    List<Person> selectAll() {
        return LogInjector.infoLog(() -> onRequest(
            () -> jdbcTemplate.query("SELECT id, name, surname, passport, town FROM hse.person", personRowMapper)
        ));
    }

    List<Person> selectFirstN(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
            () -> jdbcTemplate.query(
                "SELECT id, name, surname, passport, town FROM hse.person LIMIT :amount", parameters, personRowMapper)
        ), parameters);
    }

    Person selectPersonById(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
            () -> jdbcTemplate.queryForObject(
                "SELECT id, name, surname, passport, town FROM hse.person WHERE id = :id", parameters, personRowMapper)
        ), parameters);
    }

    void insertPerson(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
            () -> jdbcTemplate.update("INSERT INTO hse.person (id, name, surname, passport, town) "
                                          + "VALUES(:id, :name, :surname, :passport, :town)", parameters)
        ), parameters);
    }

    private <R> R onRequest(Supplier<R> getDataCallback) {
        try {
            log.info("Trying to query database");
            return getDataCallback.get();
        } catch (DataAccessException e) {
            log.info("Error occurred while trying to execute query to db, with message: {}", e.getMessage());
            return null;
        }
    }
}
