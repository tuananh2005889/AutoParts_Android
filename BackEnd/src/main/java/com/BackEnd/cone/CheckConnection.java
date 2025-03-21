package com.BackEnd.cone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckConnection implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public CheckConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("SELECT 1");
            System.out.println("Successfully");
        } catch (Exception e) {
            System.err.println("Failed" + e.getMessage());
        }
    }
}