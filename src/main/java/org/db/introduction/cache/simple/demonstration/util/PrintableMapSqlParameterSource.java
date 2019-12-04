package org.db.introduction.cache.simple.demonstration.util;

import lombok.EqualsAndHashCode;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.SPACE;

@EqualsAndHashCode
public class PrintableMapSqlParameterSource extends MapSqlParameterSource {

    private static final String DELIMITER = "->";

    @Override
    public String toString() {
        return getValues().entrySet().stream()
            .map(kv -> String.join(
                DELIMITER,
                kv.getKey(), ofNullable(kv.getValue()).map(Object::toString).orElse(null))
            ).collect(Collectors.joining(SPACE));
    }
}
