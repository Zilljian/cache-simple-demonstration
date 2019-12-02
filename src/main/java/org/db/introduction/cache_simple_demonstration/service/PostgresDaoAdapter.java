package org.db.introduction.cache_simple_demonstration.service;

import lombok.RequiredArgsConstructor;
import org.db.introduction.cache_simple_demonstration.model.Person;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class PostgresDaoAdapter {

    private final PostgresDao postgresDao;

    public List<Person> getAll() {
        return ofNullable(postgresDao.selectAll())
            .orElse(Collections.emptyList());
    }

    public List<Person> getFirstN(int amount) {
        return ofNullable(postgresDao.selectFirstN(amount))
            .orElse(Collections.emptyList());
    }
}
