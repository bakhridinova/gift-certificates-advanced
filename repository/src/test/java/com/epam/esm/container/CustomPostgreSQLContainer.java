package com.epam.esm.container;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * custom postgreSQL container
 *
 * @see <a href="https://www.baeldung.com/spring-boot-testcontainers-integration-test">source</a>
 * @author bakhridinova
 */

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static CustomPostgreSQLContainer container;

    private CustomPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CustomPostgreSQLContainer();
            container.withInitScript("db/initScript.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
