package org.db.introduction.cache.simple.demonstration.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    private Long id;
    private String name;
    private String surname;
    private String passport;
    private String town;
}
