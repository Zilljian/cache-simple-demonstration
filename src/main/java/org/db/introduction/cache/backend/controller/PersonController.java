package org.db.introduction.cache.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.backend.model.Person;
import org.db.introduction.cache.backend.model.Student;
import org.db.introduction.cache.backend.service.PostgresDaoAdapter;
import org.db.introduction.cache.backend.util.LogInjector;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PostgresDaoAdapter postgresDaoAdapter;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> selectAllPersons(@RequestParam String database) {
        log.info("Received request for selecting all in persons with database = {}", database);
        return LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.getAllPersons(database));
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertEntryPerson(@RequestParam String database,
                                  @RequestBody Person person) {
        log.info("Received request for inserting in persons with database = {} and entry = {}", database, person);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.insertEntryPerson(database, person));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateEntryPerson(@RequestParam String database,
                                  @RequestBody Person person) {
        log.info("Received request for updating entry in persons with database = {} and entry = {}", database, person);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.updateEntryPerson(database, person));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> searchStringInPerson(@RequestParam String database,
                                             @RequestParam String searchString) {
        log.info("Received request for searching in persons with database = {} and search string = {}", database, searchString);
        return LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.searchStringInPerson(database, searchString));
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public void removeEntryPerson(@RequestParam String database,
                                  @PathVariable("id") Long id) {
        log.info("Received request for removing entry in persons with database = {} and id = {}", database, id);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.removeEntryPerson(database, id));
    }

    @DeleteMapping
    public void removeAllPersons(@RequestParam String database) {
        log.info("Received request for removing all in persons with database = {}", database);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.removeAllPersons(database));
    }
}
