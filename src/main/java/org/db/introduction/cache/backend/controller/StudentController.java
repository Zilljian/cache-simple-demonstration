package org.db.introduction.cache.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.backend.model.Student;
import org.db.introduction.cache.backend.service.PostgresDaoAdapter;
import org.db.introduction.cache.backend.util.LogInjector;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final PostgresDaoAdapter postgresDaoAdapter;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> selectAllStudents(@RequestParam String database) {
        log.info("Received request for selecting all in students with database = {}", database);
        return LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.getAllStudents(database));
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertEntryStudent(@RequestParam String database,
                                   @RequestBody Student student) {
        log.info("Received request for inserting in students with database = {} and entry = {}", database, student);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.insertEntryStudent(database, student));
    }

    @ResponseBody
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateEntryStudent(@RequestParam String database,
                                   @RequestBody Student student) {
        log.info("Received request for updating in students with database = {} and entry = {}", database, student);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.updateEntryStudent(database, student));
    }

    @ResponseBody
    @GetMapping(value = "/search}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> searchStringInStudent(@RequestParam String database,
                                               @RequestParam String searchString) {
        log.info("Received request for searching in students with database = {} and search string = {}", database, searchString);
        return LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.searchStringInStudent(database, searchString));
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public void removeEntryStudent(@RequestParam String database,
                                   @PathParam("id") Long id) {
        log.info("Received request for removing entry in students with database = {} and id = {}", database, id);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.removeEntryStudent(database, id));
    }

    @DeleteMapping
    public void removeAllStudents(@RequestParam String database) {
        log.info("Received request for searching in students with database = {}", database);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.removeAllStudents(database));
    }
}
