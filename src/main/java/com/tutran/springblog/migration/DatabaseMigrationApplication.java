package com.tutran.springblog.migration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class DatabaseMigrationApplication implements CommandLineRunner {
    private final Flyway flyway;

    @Autowired
    public DatabaseMigrationApplication(Flyway flyway) {
        this.flyway = flyway;
    }

    public static void main(String[] args) {
        SpringApplication.run(DatabaseMigrationApplication.class, args);
    }

    @Override
    public void run(String... args) {
        migrateDatabase();
    }

    private void migrateDatabase() {
        flyway.migrate();
    }
}
