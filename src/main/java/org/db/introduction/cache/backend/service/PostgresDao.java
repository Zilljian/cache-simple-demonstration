package org.db.introduction.cache.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.model.Student;
import org.db.introduction.cache.backend.util.LogInjector;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<Person> selectAllPersons(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT * FROM selectallperson(:database)",
                        parameters, new Person.RowMapper())
        ));
    }

    List<Person> searchStringInPerson(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT * FROM searchstringinperson(:database, :searchString)",
                        parameters, new Person.RowMapper())
        ));
    }

    List<Student> searchStringInStudent(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT * FROM searchstringinstudent(:database, :searchString)",
                        parameters, new Student.RowMapper())
        ));
    }

    List<Student> selectAllStudents(MapSqlParameterSource parameters) {
        return LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT * FROM selectallstudent(:database)",
                        parameters, new Student.RowMapper())
        ));
    }

    void createDatabaseSystem(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT createdatabasesystem(:database)",
                        parameters, ResultSet::rowInserted)
        ));
    }

    void deleteDatabaseSystem(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT deletedatabasesystem(:database)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void removeAllPersons(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT removeallperson(:database)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void removeAllStudents(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT removeallstudent(:database)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void removeEntryPerson(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT removeentryperson(:database, :id)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void removeEntryStudent(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT removeentrystudent(:database, :id)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void updateEntryPerson(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT " +
                                "updateentryperson(:database, :id, :name, :surname, :passport, :phone)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void updateEntryStudent(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT updateentrystudent(:database, :id, :university, :phone::varchar)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void insertEntryPerson(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT insertentryperson(:database, :name, :surname, :passport, :phone)",
                        parameters, ResultSet::rowInserted)
        ));
    }

    void insertEntryStudent(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT insertentrystudent(:database, :phone::varchar, :personId, :university)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    void clearDatabaseSystem(MapSqlParameterSource parameters) {
        LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.query("SELECT cleardatabasesystem(:database)",
                        parameters, ResultSet::rowDeleted)
        ));
    }

    List<String> selectExistingDatabases() {
        return LogInjector.infoLog(() -> onRequest(
                () -> jdbcTemplate.queryForList("SELECT selectexistingdatabases()",
                        new PrintableMapSqlParameterSource(), String.class)
        ));
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
