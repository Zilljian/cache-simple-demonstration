CREATE OR REPLACE FUNCTION createDatabaseSystem(schemaName text) RETURNS void
AS
$$
BEGIN
    EXECUTE format(
            'CREATE SCHEMA IF NOT EXISTS %I;

            CREATE TABLE IF NOT EXISTS %I.person
            (   id          BIGSERIAL PRIMARY KEY,
                name        VARCHAR(50)  NOT NULL,
                surname     VARCHAR(255) NOT NULL,
                passport    VARCHAR(255) UNIQUE,
                phone       VARCHAR(15),
                external_id BIGSERIAL UNIQUE,
                created     TIMESTAMP default now(),
                updated     TIMESTAMP default now()
            );

            CREATE TABLE IF NOT EXISTS %I.student
            (   id        BIGSERIAL,
                university VARCHAR(123) NOT NULL,
                phone     BOOLEAN DEFAULT FALSE,
                person_id BIGINT UNIQUE REFERENCES %I.person (external_id)
            );', schemaName, schemaName, schemaName, schemaName
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION deleteDatabaseSystem(schemaName text) RETURNS void
AS
$$
BEGIN
    IF schemaName = 'public' THEN
        RAISE NOTICE 'It is restricted to drop public schema';
        RETURN;
    ELSE
        EXECUTE format(
                'DROP SCHEMA CASCADE %I;', schemaName
            );
    END IF;
END ;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION selectAllStudent(schemaName text)
    RETURNS TABLE
            (
                id         bigint,
                phone      boolean,
                person_id  bigint,
                university varchar
            )
AS
$$
BEGIN
    RETURN QUERY EXECUTE format(
            'SELECT * FROM %I.student', schemaName
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION selectAllPerson(schemaName text)
    RETURNS TABLE
            (
                id          bigint,
                name        varchar,
                surname     varchar,
                passport    varchar,
                phone       varchar,
                external_id bigint,
                updated     timestamp,
                created     timestamp
            )
AS
$$
BEGIN
    RETURN QUERY EXECUTE format(
            'SELECT * FROM %I.person', schemaName
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION removeAllPerson(schemaName text)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format(
            'DELETE FROM %I.person', schemaName
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION removeAllStudent(schemaName text)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format(
            'DELETE FROM %s.student', schemaName
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insertEntryStudent(schemaName text, phone varchar, personId bigint, university varchar)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format('insert into %I.student (phone, person_id, university) ' ||
                   'values(%s, %s, ' || quote_literal('%s') || ')', schemaName, phone, personId, university
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insertEntryPerson(schemaName text, name varchar,
                                             surname varchar, passport varchar, phone varchar)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format('insert into %I.person (name, surname, passport, phone) ' ||
                   'values(' || quote_literal('%s') || ', ' || quote_literal('%s') || ', ' ||
                   quote_literal('%s') || ', ' || quote_literal('%s') || ')'
        , schemaName, name, surname, passport, phone
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION updateEntryPerson(schemaName text, id bigint,
                                             name varchar, surname varchar,
                                             passport varchar, phone varchar)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format('UPDATE %I.person SET
                   name = ' || quote_literal('%s') ||
                   ', surname = ' || quote_literal('%s') ||
                   ', passport = ' || quote_literal('%s') ||
                   ', phone = ' || quote_literal('%s') ||
                   ', updated = now()
                   WHERE id = %s;', schemaName, name, surname,
                   passport, phone, id
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION updateEntryStudent(schemaName text, id bigint,
                                              university varchar, phone varchar)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format('UPDATE %I.student SET
                    phone = %s, university = ' || quote_literal('%s') ||
                   ' WHERE id = %s;', schemaName, phone, university, id
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION removeEntryStudent(schemaName text, id bigint)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format(
            'DELETE FROM %I.student WHERE id = %s;', schemaName, id
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION removeEntryPerson(schemaName text, id bigint)
    RETURNS void
AS
$$
BEGIN
    EXECUTE format(
            'DELETE FROM %I.person
            WHERE id = %s', schemaName, id
        );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION searchStringInPerson(schemaName text, searchString text)
    RETURNS TABLE
            (
                id          bigint,
                name        varchar,
                surname     varchar,
                passport    varchar,
                phone       varchar,
                external_id bigint,
                updated     timestamp,
                created     timestamp
            )
AS
$$
BEGIN
    RETURN QUERY EXECUTE
        format('SELECT * FROM %I.person' ||
               ' WHERE name ILIKE ' || quote_literal('%%%s%%') ||
               ' OR surname ILIKE ' || quote_literal('%%%s%%') ||
               ' OR passport ILIKE ' || quote_literal('%%%s%%') ||
               ' OR phone ILIKE ' || quote_literal('%%%s%%'),
               schemaName, searchString, searchString,
               searchString, searchString, searchString
            );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION searchStringInStudent(schemaName text, searchString text)
    RETURNS TABLE
            (
                id        bigint,
                university varchar,
                phone     boolean,
                person_id bigint
            )
AS
$$
BEGIN
    RETURN QUERY EXECUTE
        format('SELECT * FROM %I.student
                WHERE university ILIKE ' || quote_literal('%%%s%%') || ';',
               schemaName, searchString
            );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION clearDatabaseSystem(schemaName text)
    RETURNS VOID
AS
$$
BEGIN
    EXECUTE
        format('DELETE FROM %I.student;' ||
               'DELETE FROM %I.person;',
               schemaName, schemaName
            );
END;
$$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION selectExistingDatabases()
    RETURNS SETOF varchar
AS
$$
BEGIN
    RETURN QUERY SELECT CAST(schema_name AS varchar)
                 FROM information_schema.schemata
                 WHERE schema_name != CAST('pg_toast' AS information_schema.sql_identifier)
                   AND schema_name != CAST('pg_temp_1' AS information_schema.sql_identifier)
                   AND schema_name != CAST('pg_toast_temp_1' AS information_schema.sql_identifier)
                   AND schema_name != CAST('pg_catalog' AS information_schema.sql_identifier)
                   AND schema_name != CAST('public' AS information_schema.sql_identifier)
                   AND schema_name != CAST('information_schema' AS information_schema.sql_identifier);
END;
$$
    LANGUAGE plpgsql;

CREATE USER ui_user WITH PASSWORD 'ui-password';
grant all on database hse to ui_user;
GRANT EXECUTE ON FUNCTION selectAllPerson(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION selectAllStudent(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION removeAllStudent(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION removeAllPerson(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION updateEntryPerson(schemaName text, id bigint, name varchar, surname varchar, passport varchar, phone varchar) TO ui_user;
GRANT EXECUTE ON FUNCTION updateEntryStudent(schemaName text, id bigint, phone boolean) TO ui_user;
GRANT EXECUTE ON FUNCTION removeEntryPerson(schemaName text, id bigint) TO ui_user;
GRANT EXECUTE ON FUNCTION removeEntryStudent(schemaName text, id bigint) TO ui_user;
GRANT EXECUTE ON FUNCTION insertEntryStudent(schemaName text, phone varchar, personId bigint, university varchar) TO ui_user;
GRANT EXECUTE ON FUNCTION insertEntryPerson(schemaName text, name varchar, surname varchar, passport varchar, phone varchar) TO ui_user;
GRANT EXECUTE ON FUNCTION deleteDatabaseSystem(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION createDatabaseSystem(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION searchStringInPerson(schemaName text, searchString text) TO ui_user;
GRANT EXECUTE ON FUNCTION searchStringInStudent(schemaName text, searchString text) TO ui_user;
GRANT EXECUTE ON FUNCTION clearDatabaseSystem(schemaName text) TO ui_user;
GRANT EXECUTE ON FUNCTION selectExistingDatabases() TO ui_user;
