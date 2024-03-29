package org.db.introduction.cache.backend.configuration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisAsyncCommands<String, String> redisClient(RedisURI redisURI) {
        return RedisClient
            .create(redisURI)
            .connect()
            .async();
    }

    @Bean
    RedisURI redisURI(@Value("${spring.redis.host}") String host,
                      @Value("${spring.redis.port}") int port) {
        return RedisURI.Builder
            .redis(host)
            .withPort(port)
            .withDatabase(1)
            .build();
    }
}
