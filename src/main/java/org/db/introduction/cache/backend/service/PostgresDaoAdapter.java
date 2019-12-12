package org.db.introduction.cache.backend.service;

import lombok.RequiredArgsConstructor;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.model.Student;
import org.db.introduction.cache.backend.util.PrintableMapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class PostgresDaoAdapter {

    private final PostgresDao postgresDao;

    public List<Person> getAllPersons(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        return ofNullable(postgresDao.selectAllPersons(parameters))
                .orElse(Collections.emptyList());
    }

    public List<Person> searchStringInPerson(String database, String searchString) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database)
                .addValue("searchString", searchString);
        return ofNullable(postgresDao.searchStringInPerson(parameters))
                .orElse(Collections.emptyList());
    }

    public List<Student> searchStringInStudent(String database, String searchString) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database)
                .addValue("searchString", searchString);
        return ofNullable(postgresDao.searchStringInStudent(parameters))
                .orElse(Collections.emptyList());
    }

    public List<Student> getAllStudents(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        return ofNullable(postgresDao.selectAllStudents(parameters))
                .orElse(Collections.emptyList());
    }

    public void createDatabaseSystem(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        postgresDao.createDatabaseSystem(parameters);
    }

    public void deleteDatabaseSystem(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        postgresDao.deleteDatabaseSystem(parameters);
    }

    public void removeAllPersons(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        postgresDao.removeAllPersons(parameters);
    }

    public void removeAllStudents(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        postgresDao.removeAllStudents(parameters);
    }

    public void removeEntryPerson(String database, Long id) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database)
                .addValue("id", id);
        postgresDao.removeEntryPerson(parameters);
    }

    public void removeEntryStudent(String database, Long id) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database)
                .addValue("id", id);
        postgresDao.removeEntryStudent(parameters);
    }

    public void updateEntryPerson(String database, Person person) {
        var parameters = person.getParameters()
                .addValue("database", database);
        postgresDao.removeEntryPerson(parameters);
    }

    public void insertEntryStudent(String database, Student student) {
        var parameters = student.getParameters()
                .addValue("database", database);
        postgresDao.insertEntryStudent(parameters);
    }

    public void insertEntryPerson(String database, Person person) {
        var parameters = person.getParameters()
                .addValue("database", database);
        postgresDao.insertEntryPerson(parameters);
    }

    public void updateEntryStudent(String database, Student student) {
        var parameters = student.getParameters()
                .addValue("database", database);
        postgresDao.removeEntryStudent(parameters);
    }

    public void clearDatabaseSystem(String database) {
        var parameters = new PrintableMapSqlParameterSource()
                .addValue("database", database);
        postgresDao.clearDatabaseSystem(parameters);
    }

    public List<String> selectExistingDatabases() {
        return ofNullable(postgresDao.selectExistingDatabases())
                .orElse(Collections.emptyList());
    }
}
