package org.db.introduction.cache.simple.demonstration.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.db.introduction.cache.simple.demonstration.service.PostgresDaoAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainLogic implements CommandLineRunner {
    private final PostgresDaoAdapter postgresDaoAdapter;

    void process() {
        var persons = postgresDaoAdapter.getFirstN(20);
        log.info("Received persons {}", persons);
    }

    @Override
    public void run(String... args) throws Exception {
        process();
    }
}
