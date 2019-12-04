package org.db.introduction.cache.simple.demonstration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.simple.demonstration.model.Person;
import org.db.introduction.cache.simple.demonstration.service.PostgresDaoAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.db.introduction.cache.simple.demonstration.util.LogInjector.errorAwareInfoLog;

@Slf4j
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PostgresDaoAdapter postgresDaoAdapter;

    @ResponseBody
    @GetMapping(value = "/{id:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPerson(@PathVariable("id") long id) {
        log.info("Received request for entry with id = {}", id);
        var person = errorAwareInfoLog(() -> postgresDaoAdapter.getPersonById(id));
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addPerson(@RequestBody Person person) {
        log.info("Received request for inserting with body = {}", person);
        errorAwareInfoLog(() -> postgresDaoAdapter.insertPerson(person));
    }
}
