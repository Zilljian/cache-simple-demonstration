package org.db.introduction.cache_simple_demonstration.model;

import lombok.Data;

@Data
public class PostgresConnectionProperties {
    private String driver;
    private String url;
    private String password;
    private String login;
}
