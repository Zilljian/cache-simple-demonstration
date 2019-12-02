package org.db.introduction.cache.simple.demonstration.model;

import lombok.Data;

@Data
public class PostgresConnectionProperties {
    private String driver;
    private String url;
    private String password;
    private String login;
}
