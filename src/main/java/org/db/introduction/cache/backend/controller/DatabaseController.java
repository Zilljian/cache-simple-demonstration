package org.db.introduction.cache.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.backend.service.PostgresDaoAdapter;
import org.db.introduction.cache.backend.util.LogInjector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/database")
@RequiredArgsConstructor
public class DatabaseController {

    private final PostgresDaoAdapter postgresDaoAdapter;

    @DeleteMapping
    public void deleteDatabaseSystem(@RequestParam String database) {
        log.info("Received request for deleting database with name [{}]", database);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.deleteDatabaseSystem(database));
    }

    @PatchMapping
    public void clearDatabaseSystem(@RequestParam String database) {
        log.info("Received request for clearing all in [{}] database", database);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.clearDatabaseSystem(database));
    }

    @GetMapping
    @ResponseBody
    public List<String> selectExistingDatabases() {
        log.info("Received request for selecting all databases");
        return LogInjector.errorAwareInfoLog(postgresDaoAdapter::selectExistingDatabases);
    }

    @PostMapping
    public void createDatabaseSystem(@RequestParam String database) {
        log.info("Received request for creating database with name [{}]", database);
        LogInjector.errorAwareInfoLog(
                () -> postgresDaoAdapter.createDatabaseSystem(database));
    }
}
