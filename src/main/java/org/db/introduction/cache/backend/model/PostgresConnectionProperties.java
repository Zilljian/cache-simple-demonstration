package org.db.introduction.cache.backend.model;

import lombok.Data;

@Data
public class PostgresConnectionProperties {

    private String driver;
    private String url;
    private String password;
    private String login;
}
