package org.db.introduction.cache.simple.demonstration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.simple.demonstration.mapper.PersonRowMapper;
import org.db.introduction.cache.simple.demonstration.model.Person;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresDao {

    private final JdbcTemplate jdbcTemplate;
    private final PersonRowMapper personRowMapper;

    List<Person> selectAll() {
        return onRequest(
            () -> jdbcTemplate.query(
                "SELECT id, name, surname, passport, town FROM hse.person",
                new Object[]{},
                personRowMapper)
        );
    }

    List<Person> selectFirstN(int amount) {
        return onRequest(
            () -> jdbcTemplate.query(
                "SELECT id, name, surname, passport, town FROM hse.person LIMIT " + amount,
                new Object[]{},
                personRowMapper), amount
        );
    }

    private <R> R onRequest(Supplier<R> getDataCallback, Object... parameters) {
        try {
            log.info("Trying to fetch data from DB with parameters: {}",
                     ofNullable(parameters)
                         .map(Objects::toString)
                         .orElse("no parameters"));
            return getDataCallback.get();
        } catch (DataAccessException e) {
            return null;
        }
    }
}
